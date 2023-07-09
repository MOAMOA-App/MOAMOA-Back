package org.zerock.moamoa.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.common.file.ImageService;
import org.zerock.moamoa.common.file.dto.FileResponse;
import org.zerock.moamoa.domain.DTO.product.*;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
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
    public ProductResponse saveProduct(ProductSaveRequest request, Long sellerId, MultipartFile[] images) {
        Product product = productMapper.toEntity(request);
        User user = userService.findById(sellerId);
        product.addUser(user);
        product = productRepository.save(product);
        List<FileResponse> responses = imageService.saveFiles(images, product.getId());
        product.updateImage(responses.size());
        product.addUser(user);    // YJ: 만약 이거랑 sellerid 받은것도 내꺼면 그냥 지워도 될듯 product.getUser() 쓰기!!!
        // request.getUser를 써야 하나...
        product.addUserPosts(user);
        return productMapper.toDto(productRepository.save(product));
    }


    @Transactional
    public boolean remove(Long id) {
        Product product = findById(id);

        // 유저의 만든공구 리스트에서 공구 제거
        User user = product.getUser();
        product.removeUserPosts(user);
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
        Pageable itemPage =  PageRequest.of(pageNo, pageSize);
        User user = userService.findById(uid);
        Page<Product> productPage = productRepository.findByUser(user, itemPage);
        return productPage.map(product -> findOne(product.getId()));
    }

    // 참여공구 리스트
    // 아니근데일케하면 party 생성일 씹히는거아닌가 일단 테스트좀해봐야됨
    // 순환경고떠서 일단 킵
//	public Page<ProductResponse> toResParty(Long uid,
//											String orderBy, String sortOrder, int pageNo, int pageSize) {
//		List<PartyResponse> partyRes = partyService.getByBuyer(uid);
//		List<Product> products = new ArrayList<>();
//		for (PartyResponse partyResponse : partyRes){
//			Product product = partyResponse.getProduct();
//			products.add(product);
//		}
//
//		Sort sort;
//		if (sortOrder.equalsIgnoreCase("desc")) {
//			sort = Sort.by(Sort.Direction.DESC, "party.createdAt");
//		} else {
//			sort = Sort.by(Sort.Direction.ASC, "party.createdAt");
//		}
//
//		List<ProductResponse> list = products.stream()
//										.map(productMapper::toDto)
//										.toList();
//
//		Page<ProductResponse> productPage = listtoPage(list, sort, pageNo, pageSize);
//		return productPage.map(product -> findOne(product.getId()));
//	}

    // 리스트 -> 페이지 변환
//    public Page<ProductResponse> listtoPage(List<ProductResponse> list, Sort sort,
//                                            int pageNo, int pageSize) {
//
//        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);
//        int start = (int) pageRequest.getOffset();    // 페이지의 시작 인덱스. Offset: 페이지 시작 위치
//        int end = Math.min((start + pageRequest.getPageSize()), list.size());    // 페이지의 끝 인덱스
//        // end값 list 크기랑 비교, 만약 end>list -> end 값을 list의 크기로 대체 -> 리스트의 범위 초과하는 페이지 요청 방지
//
//        return new PageImpl<>(list.subList(start, end), pageRequest, list.size());
//    }


//	public List<ProductResponse> toResPostList(Long uid) {
//		User user = userService.findById(uid);
//		return user.getMyPosts()
//				.stream()
//				.map(productMapper::toDto)
//				.toList();
//	}
}