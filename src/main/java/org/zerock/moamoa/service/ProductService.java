package org.zerock.moamoa.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.product.*;
import org.zerock.moamoa.domain.DTO.productImage.ImageMapper;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.ProductImages;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ProductImageRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.utils.file.Folder;
import org.zerock.moamoa.utils.file.ImageUtils;
import org.zerock.moamoa.utils.file.dto.FileResponse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductImageRepository imageRepository;
    private final ProductMapper productMapper;
    private final UserRepository userRepository;
    private final UserService userService;
    private final WishListService wishListService;
    private static final String PRODUCT_FILE_URL = Folder.PRODUCT.getFolder();

    public ProductResponse findOne(Long id) {
        return productMapper.toDto(findById(id));
    }

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    @Transactional
    public ProductResponse saveProduct(ProductSaveRequest request, MultipartFile[] images, String username) {
        User user = userRepository.findByEmailOrThrow(username);
        request.setUser(user);

        Product product = productRepository.save(productMapper.toEntity(request));

        if (images != null) {
            List<ProductImages> articleImages = saveImage(images, product);
            product.setProductImages(articleImages);
        }
        return productMapper.toDto(product);
    }

    @Transactional
    public boolean remove(Long id, String username) {
        Product product = findById(id);
        User user = userRepository.findByEmailOrThrow(username);
        checkAuth(product, user);
        removeImage(product);
        imageRepository.deleteAll(product.getProductImages());
        product.delete();
        return !product.getActivate();
    }

    @Transactional
    // .save, remove -> 함수자체에서 트랜잭션  -> 어노테이션을 쓸 필요가 없는것
    public ProductResponse updateInfo(Long pid, ProductUpdateRequest request, MultipartFile[] images, String username) {
        Product product = findById(pid);
        User user = userRepository.findByEmailOrThrow(username);
        checkAuth(product, user);

        removeImage(product);
        imageRepository.deleteAll(product.getProductImages());
        if (images != null) {
            List<ProductImages> articleImages = saveImage(images, product);
            product.setProductImages(articleImages);
        } else {
            product.setProductImages(new ArrayList<>());
        }
        product.updateInfo(request);
        return productMapper.toDto(product);
    }

    @Transactional
    public ProductResponse updateStatus(Long pid, ProductStatusUpdateRequest request, String username) {
        Product product = findById(pid);
        User user = userRepository.findByEmailOrThrow(username);
        checkAuth(product, user);
        product.updateStatus(request.getStatus());
        return productMapper.toDto(product);
    }

    public Page<ProductResponse> search(
            String title, String description,
            List<String> categories, List<String> statuses,
            String orderBy, String sortOrder,
            int pageNo, int pageSize
    ) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (description != null && !description.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")),
                        "%" + description.toLowerCase() + "%"));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").in(categories));
            }
            if (statuses != null && !statuses.isEmpty()) {
                predicates.add(root.get("status").in(statuses));
            }
            predicates.add(root.get("activate").in(true));
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort;
        if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, orderBy);
        } else {
            sort = Sort.by(Sort.Direction.ASC, orderBy);
        }
        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
        Page<Product> resultPage = productRepository.findAll(spec, pageRequest);
        //Pageable itemPage =  PageRequest.of(pageNo, pageSize);
        return resultPage.map(product -> findOne(product.getId()));
    }

    // 만든공구 리스트
    public Page<ProductResponse> toResPost(Long uid, int pageNo, int pageSize) {
        User user = userService.findById(uid);
//        if (user == null) {
//            throw new EntityNotFoundException(ErrorCode.USER_NOT_FOUND);
//        }

        Pageable itemPage = PageRequest.of(pageNo, pageSize);
        Page<Product> productPage = productRepository.findByUser(user, itemPage);
        if (productPage.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return productPage.map(product -> findOne(product.getId()));
    }

    // 참여공구 리스트
    // partyService 불러서 깔끔하게 만들고싶은데 자꾸 순환오류남...
    public Page<ProductResponse> toResParty(Long uid, int pageNo, int pageSize) {
        User buyer = userService.findById(uid);
//        if (userService.isUserExits(buyer)) {
//            throw new EntityNotFoundException(ErrorCode.USER_NOT_FOUND);
//        }

        List<Party> parties = buyer.getParties();
        if (parties.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        // 상품 가져와서 리스트 추가
        List<Product> products = new ArrayList<>();

        for (Party party : parties) {
            Product product = party.getProduct();
            products.add(product);
        }

        List<ProductResponse> list = products.stream()
                .map(productMapper::toDto)
                .toList();

        Sort sort = Sort.by(Sort.Direction.DESC, "party.createdAt");
        Page<ProductResponse> productPage = listtoPage(list, sort, pageNo, pageSize);
        return productPage.map(product -> findOne(product.getId()));
    }

    // 찜한공구 리스트
    public Page<ProductResponse> toResWish(Long uid, int pageNo, int pageSize) {
        Sort sort = Sort.by(Sort.Direction.DESC, "wishlist.createdAt");
        List<ProductResponse> list = wishListService.wishToProduct(uid).stream()
                .map(productMapper::toDto)
                .toList();
        Page<ProductResponse> productPage = listtoPage(list, sort, pageNo, pageSize);
        return productPage.map(product -> findOne(product.getId()));
    }

    // 리스트 -> 페이지 변환
    public Page<ProductResponse> listtoPage(List<ProductResponse> list, Sort sort, int pageNo, int pageSize) {

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
        int start = (int) pageRequest.getOffset();    // 페이지의 시작 인덱스. Offset: 페이지 시작 위치
        int end = Math.min((start + pageRequest.getPageSize()), list.size());    // 페이지의 끝 인덱스
        // end값 list 크기랑 비교, 만약 end>list -> end 값을 list의 크기로 대체 -> 리스트의 범위 초과하는 페이지 요청 방지

        return new PageImpl<>(list.subList(start, end), pageRequest, list.size());
    }

    public void checkAuth(Product product, User user) {
        if (!product.getUser().equals(user)) throw new AuthException(ErrorCode.PRODUCT_AUTH_FAIL);
    }


    private void removeImage(Product product) {
        List<FileResponse> images = product.getProductImages().stream().map(ImageMapper.INSTANCE::toDto).toList();

        for (FileResponse temp : images) {
            ImageUtils.removeFile(temp.getFileName(), PRODUCT_FILE_URL);
        }
    }

    @Transactional
    private List<ProductImages> saveImage(MultipartFile[] images, Product product) {
        List<FileResponse> responses = Arrays.stream(images)
                .map(file -> ImageUtils.saveFile(file, PRODUCT_FILE_URL, file.getOriginalFilename().split("\\.")[0]))
                .toList();

        responses.forEach(temp -> temp.setProduct(product));

        return responses.stream()
                .map(ImageMapper.INSTANCE::toEntity)
                .map(imageRepository::save)
                .collect(Collectors.toList());
    }
}