package org.zerock.moamoa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


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
    public Optional<Product> findById(Long id){
        return this.productRepository.findById(id);
    }

    @Transactional
    public List<Product> findAll(){
        return this.productRepository.findAll();
    }

    @Transactional
    public Product saveProduct(Product product, Long sellerId) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new IllegalArgumentException("Seller not found"));

        product.setUser(seller);

        return productRepository.save(product);
    }
    @Transactional
    public void removeProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        this.productRepository.delete(product);
    }
    @Transactional
    public Product findProductById(Long id) {
        // ID를 이용하여 Product를 조회하는 로직을 구현
        // 예시로는 JpaRepository를 사용하여 데이터베이스에서 조회하는 방식을 사용하였습니다.
        return productRepository.findById(id)
                 .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }
    @Transactional
    public Product updateProduct(Long id, String UID){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        product.setId(id);
        return this.productRepository.save(product);
    }

}