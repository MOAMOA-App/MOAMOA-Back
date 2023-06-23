package org.zerock.moamoa.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;
import org.zerock.moamoa.repository.WishListRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;


@Service
public class WishListService {
    private final WishListRepository wishListRepository;
    private final ProductService productService;
    private final UserService userService;

    @Autowired
    public WishListService(WishListRepository wishListRepository, ProductService productService, UserService userService) {
        this.wishListRepository = wishListRepository;
        this.productService = productService;
        this.userService = userService;
    }


    @Transactional
    public Optional<WishList> findById(Long id){
        return this.wishListRepository.findById(id);
    }

    @Transactional
    public List<WishList> findAll(){
        return this.wishListRepository.findAll();
    }

    @Transactional
    public WishList saveWish(Long user_id, Long product_id){
        WishList wishList = new WishList();
        User user = userService.findById(user_id).orElseThrow(()->new IllegalArgumentException("해당 사용자가 없습니다. id=" + user_id));
        Product product = productService.findById(product_id);
        if (product == null) {
            throw new IllegalArgumentException("해당 상품이 없습니다. id=" + product_id);
        }
        wishList.setUserId(user);
        wishList.setProductId(product);
        user.addWishList(wishList); // user의 wishlists에 정보 추가
        return wishListRepository.save(wishList);
    }

    @Transactional
    public void removeWish(Long id){
        WishList wishList = wishListRepository.findById(id).orElseThrow(()->new IllegalArgumentException("해당 위시리스트가 없습니다. id=" + id));

        // User의 removeWishList를 호출해서 WishList 제거하는 코드 추가해봄
        User user = wishList.getUserId();
        if (user != null) {
            user.removeWishList(wishList);
            userService.saveUser(user);
        }

        this.wishListRepository.delete(wishList);
    }

}
