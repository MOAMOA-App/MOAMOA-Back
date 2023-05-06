package org.zerock.moamoa.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.ProductRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
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
    public Product saveProduct(Product product){
        return this.productRepository.save(product);
    }

    @Transactional
    public void removeProduct(Long id){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        this.productRepository.delete(product);
    }

    @Transactional
    public Product updateProduct(Long id, String UID){
        Product product = productRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("해당 아이템이 없습니다. id=" + id));

        product.setId(id);
        return this.productRepository.save(product);
    }

}