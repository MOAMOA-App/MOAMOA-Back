package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.wishlist.WishListMapper;
import org.zerock.moamoa.domain.DTO.wishlist.WishListRequest;
import org.zerock.moamoa.domain.DTO.wishlist.WishListResponse;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.repository.WishListRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListMapper wishListMapper;
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;


    // ID에 해당하는 위시리스트 조회-> WishListMapper 사용해 WishListResponse 객체로 매핑 후 반환
    public WishListResponse findOne(Long id) {
        return wishListMapper.toDto(findById(id));
    }

    @Transactional
    public WishList findById(Long id) {
        return this.wishListRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND));
    }

    public List<WishList> findAll() {
        return this.wishListRepository.findAll();
    }

    public WishListResponse saveWish(WishListRequest request) {
        WishList wishList = wishListMapper.toEntity(request);
        User user = wishList.getUser();
        wishList.addUserWish(user);
        return wishListMapper.toDto(wishListRepository.save(wishList));
    }

    public void removeWish(Long id) {
//        if (wishListRepository.findById(id).isEmpty())
//            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);

        Optional<WishList> optionalWishList = wishListRepository.findById(id);
        if (optionalWishList.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        // optionalWishList에서 실제 WishList 객체 가져옴
        WishList wishList = optionalWishList.get();
        // WishList 객체에 연결된 User 객체 가져옴
        User user = wishList.getUser();

        if (user != null) {
            wishList.removeUserWish();
        }

        wishListRepository.deleteById(id);
    }

    public List<Product> wishToProduct(Long userId) {
        // 유저에  저장된 위시리스트 가져옴
        User user = userRepository.findByIdOrThrow(userId);
        List<WishList> wishLists = wishListRepository.findByUser(user);

        // 상품 가져와서 리스트 추가
        List<Product> products = new ArrayList<>();
        for (WishList wishList : wishLists) {
            Product product = wishList.getProduct();
            products.add(product);
        }

        if (products.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        } else {
            return products;
        }
    }
}
