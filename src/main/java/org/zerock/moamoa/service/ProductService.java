package org.zerock.moamoa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.DTO.ProductDTO;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    @Autowired
    public ProductService(ProductRepository productRepository, UserRepository userRepository) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public ProductDTO getById(Long id) {
        return convertToDTO(findById(id));
    }
    @Transactional
    public Product findById(Long id) {
        return productRepository.findById(id).orElse(new Product());

    }
    @Transactional
    public List<ProductDTO> getList() {
        return productDTOS(findAll());
    }

    @Transactional
    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> searchProducts(String title, String description, List<String> categories, List<String> statuses, String orderBy, String sortOrder, int pageNo, int pageSize) {
        Specification<Product> spec = (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (title != null && !title.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), "%" + title.toLowerCase() + "%"));
            }
            if (description != null && !description.isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), "%" + description.toLowerCase() + "%"));
            }
            if (categories != null && !categories.isEmpty()) {
                predicates.add(root.get("category").in(categories));
            }
            if (statuses != null && !statuses.isEmpty()) {
                predicates.add(root.get("status").in(statuses));
            }
            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };

        Sort sort;
        if (sortOrder.equalsIgnoreCase("desc")) {
            sort = Sort.by(Sort.Direction.DESC, orderBy);
        } else {
            sort = Sort.by(Sort.Direction.ASC, orderBy);
        }

        PageRequest pageRequest = PageRequest.of(pageNo, pageSize, sort);

        return productRepository.findAll(spec, pageRequest);
    }


    public Product saveProduct(Product product, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다."));

        product.setUser(seller);
        return productRepository.save(product);
    }

    public boolean removeProduct(Long id){
        Product product = findById(id);
        if(product.getId()!=null){
            productRepository.delete(product);
            return true;
        }
        return false;
    }

    public void updateContents(Product product){
        productRepository.findById(product.getId()).ifPresent( temp -> {
            temp.setUpdatedAt(LocalDateTime.now());
            temp.setCategoryId(product.getCategoryId());
            temp.setSellingArea(product.getSellingArea());
            temp.setDetailArea(product.getDetailArea());
            temp.setTitle(product.getTitle());
            temp.setDescription(product.getDescription());
            temp.setSellPrice(product.getSellPrice());
            temp.setFinishedAt(product.getFinishedAt());
            temp.setMaxCount(product.getMaxCount());
            temp.setChoiceSend(product.getChoiceSend());
            productRepository.save(temp);
        });
    }

    public void updateStatus(Long id, String status){
        productRepository.findById(id).ifPresent(product -> {
            product.setStatus(status);
            productRepository.save(product);
        });
    }

    private List<ProductDTO> productDTOS(List<Product> products){
        List<ProductDTO> productDTOList = new ArrayList<>();
        for (Product product: products) {
            productDTOList.add(convertToDTO(product));
        }
        return productDTOList;
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(product);
    }

}