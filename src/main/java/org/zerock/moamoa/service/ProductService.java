package org.zerock.moamoa.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.product.ProductMapper;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.product.ProductSaveRequest;
import org.zerock.moamoa.domain.DTO.product.ProductStatusUpdateRequest;
import org.zerock.moamoa.domain.DTO.product.ProductUpdateRequest;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ProductRepository;

import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


/*
@Transactional 어노테이션은 선언된 메서드 내에서 일어난 변경 사항은 트랜잭션 컨텍스트에 저장하고, 트랜잭션의 범위 내에서 일괄적으로 처리하기 위한 어노테이션
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

	public ProductResponse saveProduct(ProductSaveRequest request, Long sellerId) {
		Product product = productMapper.toEntity(request);
		User user = userService.findById(sellerId);
		product.addUser(user);
		return productMapper.toDto(productRepository.save(product));
	}

	@Transactional
	public boolean remove(Long id) {
		Product product = findById(id);
		product.delete();
		return !product.getActivate();
	}

	@Transactional
	// .save, remove -> 함수자체에서 트랜잭션  -> 어노테이션을 쓸 필요가 없는것
	public ProductResponse updateInfo(ProductUpdateRequest product) {
		Product temp = findById(product.getId());
		temp.updateInfo(product);
		return productMapper.toDto(temp);
	}

	@Transactional
	public ProductResponse updateStatus(ProductStatusUpdateRequest product) {
		Product temp = findById(product.getId());
		temp.updateStatus(temp.getStatus());
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

		return resultPage.map(product -> findOne(product.getId()));
	}

	public List<ProductResponse> getProductsByUserId(Long userId) {
		User user = userService.findById(userId);
		return productRepository.findByUser(user).stream().map(productMapper::toDto).toList();
	}
}