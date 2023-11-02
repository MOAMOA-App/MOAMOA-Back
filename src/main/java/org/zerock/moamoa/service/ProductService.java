package org.zerock.moamoa.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.DTO.product.*;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.NoticeType;
import org.zerock.moamoa.domain.enums.ProductStatus;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final ApplicationEventPublisher eventPublisher;

    public ProductResponse findOne(Long pid) {
        return productMapper.toDto(productRepository.findByIdOrThrow(pid));
    }

    public Boolean findAuth(Long pid, String username) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);
        // user와 product seller 일치하면 return false
        return !user.equals(product.getUser());

    }

    @Transactional
    public ProductResponse saveProduct(ProductSaveRequest request, String username) {
        User user = userRepository.findByEmailOrThrow(username);
        request.setUser(user);
        Product product = productRepository.save(productMapper.toEntity(request));

        return productMapper.toDto(product);
    }

    @Transactional
    public boolean remove(ProductStatusUpdateRequest request, String username) {
        Product product = productRepository.findByIdOrThrow(request.getProductId());
        User user = userRepository.findByEmailOrThrow(username);
        checkAuth(product, user);
        product.delete();
        return !product.getActivate();
    }

    @Transactional
    // .save, remove -> 함수자체에서 트랜잭션  -> 어노테이션을 쓸 필요가 없는것
    public ProductResponse updateInfo(ProductUpdateRequest request, String username) {
        Product product = productRepository.findByIdOrThrow(request.getProductId());
        User user = userRepository.findByEmailOrThrow(username);
        checkAuth(product, user);

        product.updateInfo(request);

        // 알림 보내는 부분: NoticeRequest 작성하기?
        // 보낼 때 필요한 정보: 발신자(senderID), 수신자(receiverID), 알림타입noticeType(update), 게시글ID(referenceID)
        // 게시글ID로 파티 불러오면 될듯. 근데 이쪽에서 직접 불러오면 너무좀그렇지않나...
        // NoticeListener로 불러주면 될듯 makeNotice에서 이렇게 받아서 noticeRequest로 만들어줌
        // null인 receiverID는 Listener에서 pid로 partyList를 불러와서 채워줌 근데이거 일케해도 되나...
        // 알림 발송
        eventPublisher.publishEvent(new NoticeSaveRequest(product.getUser().getId(), null,
                NoticeType.POST_CHANGED, product.getId()));

        return productMapper.toDto(product);
    }

    @Transactional
    public ProductResponse updateStatus(ProductStatusUpdateRequest request, String username) {
        Product product = productRepository.findByIdOrThrow(request.getProductId());
        User user = userRepository.findByEmailOrThrow(username);
        checkAuth(product, user);
        product.updateStatus(request.getStatus());

        // 알림 발송
        eventPublisher.publishEvent(new NoticeSaveRequest(product.getUser().getId(), null,
                NoticeType.STATUS_CHANGED, product.getId()));

        return productMapper.toDto(product);
    }

    public Page<ProductListResponse> search(String[] keywords, List<Category> categories, List<ProductStatus> statuses,
                                            String search, String order, int pageNo, int pageSize) {

        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 검색어가 있으면 키워드에 따라 like 검색
            if (keywords.length > 0) {
                Predicate[] keywordPredicates = new Predicate[keywords.length];
                Predicate[] likePredicates;

                switch (search) {
                    case "sub" -> likePredicates = Arrays.stream(keywords)
                            .map(key -> criteriaBuilder.like(root.get("title"), "%" + key + "%"))
                            .toArray(Predicate[]::new);

                    case "descript" -> likePredicates = Arrays.stream(keywords)
                            .map(key -> criteriaBuilder.like(root.get("description"), "%" + key + "%"))
                            .toArray(Predicate[]::new);

                    default -> likePredicates = Arrays.stream(keywords)
                            .flatMap(key -> Stream.of(
                                    criteriaBuilder.like(root.get("title"), "%" + key + "%"),
                                    criteriaBuilder.like(root.get("description"), "%" + key + "%")
                            ))
                            .toArray(Predicate[]::new);
                }

                for (int i = 0; i < keywords.length; i++) keywordPredicates[i] = criteriaBuilder.or(likePredicates);

                predicates.add(criteriaBuilder.or(keywordPredicates));

            }
            //상태 카테고리 추가
            if (statuses.size() > 0) {
                Predicate[] likePredicates = statuses.stream().map(status -> criteriaBuilder.equal(root.get("status"), status)).toArray(Predicate[]::new);
                Predicate[] statusPredicates = Arrays.stream(likePredicates).map(criteriaBuilder::or).toArray(Predicate[]::new);
                predicates.add(criteriaBuilder.or(statusPredicates));
            }
            if (categories.size() > 0) {
                Predicate[] likePredicates = categories.stream().map(status -> criteriaBuilder.equal(root.get("category"), status.getLabel())).toArray(Predicate[]::new);
                Predicate[] categoryPredicates = Arrays.stream(likePredicates).map(criteriaBuilder::or).toArray(Predicate[]::new);
                predicates.add(criteriaBuilder.or(categoryPredicates));
            }

            predicates.add(criteriaBuilder.equal(root.get("activate"), true));

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Pageable pageable;
        String[] sortData = findSortField(order);

        // 정렬 설정
        if (sortData[1].equals("desc")) {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.DESC, sortData[0]));
        } else {
            pageable = PageRequest.of(pageNo, pageSize, Sort.by(Sort.Direction.ASC, sortData[0]));
        }

        Page<Product> tourCourses = productRepository.findAll(spec, pageable);
        return tourCourses.map(productMapper::toListDto);
    }

    private String[] findSortField(String order) {
        switch (order) {
            case "recent" -> {  //최신순
                return new String[]{"createdAt", "desc"};
            }
            case "oldest" -> {  //오래된순
                return new String[]{"createdAt", "asc"};
            }
            case "imminent" -> {
                return new String[]{"finishedAt", "asc"};
            }
            case "views" -> {
                return new String[]{"viewCount", "desc"};
            }
            case "popularity" -> {
                return new String[]{"", "desc"};
            }
            default -> {
                return new String[]{"createdAt", "desc"};
            }
        }
    }


    // 만든공구 리스트
    public Page<ProductResponse> findPageByUser(String username, int pageNo, int pageSize) {
        User user = userRepository.findByEmailOrThrow(username);
        Pageable itemPage = PageRequest.of(pageNo, pageSize);
        Page<Product> productPage = productRepository.findByUser(user, itemPage);

        if (productPage.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return productPage.map(productMapper::toDto);
    }

    public void checkAuth(Product product, User user) {
        if (!product.getUser().equals(user)) throw new AuthException(ErrorCode.PRODUCT_AUTH_FAIL);
    }


}