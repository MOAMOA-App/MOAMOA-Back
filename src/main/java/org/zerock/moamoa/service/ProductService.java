package org.zerock.moamoa.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.common.file.ImageService;
import org.zerock.moamoa.common.file.dto.FileResponse;
import org.zerock.moamoa.domain.DTO.product.*;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;
import org.zerock.moamoa.repository.ProductRepository;

import java.util.ArrayList;
import java.util.List;


/*
@Transactional 어노테이션은 선언된 메서드 내에서 일어난 변경 사항은 트랜잭션 컨텍스트에 저장하고,
트랜잭션의 범위 내에서 일괄적으로 처리하기 위한 어노테이션
save처럼 자체적으로 트랜잭션을 처리하지 않으면서도 메서드가 종료되면서 DB에 변경사항을 반영해야할 때 사용할 것
ex) update
	@Transactional
	public AnnounceResponse updateInfo(AnnounceRequest announce) {
		Announce temp = findById(announce.getId());
		temp.updateInfo(announce);
		return announceMapper.toDto(temp);
	}
*/

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserService userService;
    private final WishListService wishListService;
    private final ImageService imageService;

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
    public ProductResponse saveProduct(ProductSaveRequest request, MultipartFile[] images) {
        Product product = productMapper.toEntity(request);
        product = productRepository.save(product);
        List<FileResponse> responses = imageService.saveFiles(images, product.getId());
        product.updateImage(responses.size());

        User user = product.getUser();
        product.addUserPosts(user);
        return productMapper.toDto(productRepository.save(product));
    }


    @Transactional
    public boolean remove(Long id) {
        Product product = findById(id);

//        // 유저의 만든공구 리스트에서 공구 제거 -> 좀 애매...
//        User user = product.getUser();
//        product.removeUserPosts(user);
        product.delete();
        return !product.getActivate();
    }

    @Transactional
    // .save, remove -> 함수자체에서 트랜잭션  -> 어노테이션을 쓸 필요가 없는것
    public ProductResponse updateInfo(ProductUpdateRequest product, MultipartFile[] images) {
        Product temp = findById(product.getId());
        log.info(String.valueOf(images.length));
        imageService.saveFiles(images, temp.getId());
        temp.updateImage(images.length);
        temp.updateInfo(product);
        return productMapper.toDto(temp);
    }

    @Transactional
    public ProductResponse updateStatus(ProductStatusUpdateRequest product) {
        Product temp = findById(product.getId());
        temp.updateStatus(product.getStatus());
        return productMapper.toDto(temp);
    }


    @Transactional
    // .save, remove -> 함수자체에서 트랜잭션  -> 어노테이션을 쓸 필요가 없는것
    public ProductResponse updateInfo(ProductUpdateRequest product) {
        Product temp = findById(product.getId());
        temp.updateInfo(product);
        return productMapper.toDto(temp);
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

        Pageable itemPage =  PageRequest.of(pageNo, pageSize);
        Page<Product> productPage = productRepository.findByUser(user, itemPage);
        if (productPage.isEmpty()){
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
        if (parties.isEmpty()){
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
}