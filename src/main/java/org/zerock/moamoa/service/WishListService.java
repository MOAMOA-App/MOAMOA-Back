package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
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


    public List<WishList> findAll() {
        return this.wishListRepository.findAll();
    }

    public WishListResponse saveWish(WishListRequest request) {
        WishList wishList = wishListRepository.save(wishListMapper.toEntity(request));
        return wishListMapper.toDto(wishList);
    }

    public void removeWish(Long id) {
        Optional<WishList> optionalWishList = wishListRepository.findById(id);
        if (optionalWishList.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
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

    // 찜한공구 리스트
    public Page<WishListResponse> toResWish(String username, int pageNo, int pageSize) {
        User user = userRepository.findByEmailOrThrow(username);
        Pageable itemPage = PageRequest.of(pageNo, pageSize);
        Page<WishList> wishListPage = wishListRepository.findByUser(user, itemPage);
        if (wishListPage.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return wishListPage.map(wishListMapper::toDto);
    }
}
