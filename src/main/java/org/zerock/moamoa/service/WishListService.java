package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.wishlist.WishListMapper;
import org.zerock.moamoa.domain.DTO.wishlist.WishListRequest;
import org.zerock.moamoa.domain.DTO.wishlist.WishListResponse;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.repository.WishListRepository;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListMapper wishListMapper;
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;


    public List<WishList> findAll() {
        return this.wishListRepository.findAll();
    }

    public ResultResponse saveWish(String username, Long pid) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);
        WishListRequest request = new WishListRequest(product, user);
        wishListRepository.save(wishListMapper.toEntity(request));
        return ResultResponse.toDto("OK");
    }

    public ResultResponse removeWish(String username, Long pid) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(pid);
        WishList wishlist = wishListRepository.findByProductAndUser(product, user);
//
//        Optional<WishList> optionalWishList = wishListRepository.findById(pid);
//        if (optionalWishList.isEmpty()) {
//            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
//        }
        wishListRepository.deleteById(wishlist.getId());

        return ResultResponse.toDto("OK");
    }

    // 찜한공구 리스트
    public Page<WishListResponse> findPageByUser(String username, int pageNo, int pageSize) {
        User user = userRepository.findByEmailOrThrow(username);
        Pageable itemPage = PageRequest.of(pageNo, pageSize);
        Page<WishList> wishListPage = wishListRepository.findByUser(user, itemPage);
        if (wishListPage.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }
        return wishListPage.map(wishListMapper::toDto);
    }

    public List<WishListResponse> getByProduct(Long referenceID) {
        Product product = productRepository.findByIdOrThrow(referenceID);
        List<WishList> wishLists = wishListRepository.findByProduct(product);

        return wishLists.stream().map(wishListMapper::toDto).toList();
    }
}
