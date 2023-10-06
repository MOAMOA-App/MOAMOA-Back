package org.zerock.moamoa.service;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.DTO.product.*;
import org.zerock.moamoa.domain.entity.Party;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.NoticeType;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final ProductMapper productMapper;
    private final FileService fileService;
    private final ApplicationEventPublisher eventPublisher;

    public ProductResponse findOne(Long pid) {
        return productMapper.toDto(productRepository.findByIdOrThrow(pid));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findPageByUser(User user, Pageable itemPage) {
        return productRepository.findByUser(user, itemPage);
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
        return resultPage.map(productMapper::toDto);
    }

    // 만든공구 리스트
    public Page<ProductResponse> toResPost(String username, int pageNo, int pageSize) {
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