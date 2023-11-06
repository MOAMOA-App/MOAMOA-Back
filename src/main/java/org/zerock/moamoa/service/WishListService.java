package org.zerock.moamoa.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.zerock.moamoa.domain.DTO.product.ProductListResponse;
import org.zerock.moamoa.domain.DTO.product.ProductMapper;
import org.zerock.moamoa.domain.DTO.wishlist.WishListMapper;
import org.zerock.moamoa.domain.DTO.wishlist.WishListRequest;
import org.zerock.moamoa.domain.DTO.wishlist.WishListResponse;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.repository.WishListRepository;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class WishListService {
    private final WishListRepository wishListRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final WishListMapper wishListMapper;
    private final ProductMapper productMapper;

    /**
     * 위시리스트 변경
     */
    public WishListResponse changeWishList(WishListRequest request, String username) {
        User user = userRepository.findByEmailOrThrow(username);
        Product product = productRepository.findByIdOrThrow(request.getProductId());

        request.setUser(user);
        request.setProduct(product);

        if (isSameUser(request)) {
            request.setStatus(false);
            return wishListMapper.toDto(request, "CANT WISH OWN PRODUCT");
        }


        // true : 찜하기
        if (request.isStatus()) return updateTrue(request);
        // false : 찜하기 해제
        return updateFalse(request);
    }

    private WishListResponse updateFalse(WishListRequest request) {
        boolean check = isExist(request);
        if (!check) return wishListMapper.toDto(request, "ALREADY SAME STATUS");
        wishListRepository.delete(wishListRepository.findByUserAndProduct(request.getUser(), request.getProduct()));
        return wishListMapper.toDto(request, "OK");
    }

    private WishListResponse updateTrue(WishListRequest request) {
        boolean check = isExist(request);
        if (check) return wishListMapper.toDto(request, "ALREADY SAME STATUS");


        wishListRepository.save(wishListMapper.toEntity(request));

        return wishListMapper.toDto(request, "OK");
    }

    private boolean isExist(WishListRequest request) {
        return wishListRepository.existsByUserAndProduct(request.getUser(), request.getProduct());
    }

    private boolean isSameUser(WishListRequest request) {
        return request.getProduct().getUser().equals(request.getUser());
    }

    /**
     * 사용자가 찜한 상품 리스트 출력
     */
    public Page<ProductListResponse> findPageByUser(String username, int pageNo, int pageSize) {
        User user = userRepository.findByEmailOrThrow(username);
        Pageable itemPage = PageRequest.of(pageNo, pageSize);
        Page<WishList> wishListPage = wishListRepository.findByUser(user, itemPage);

        return wishListPage.map(WishList::getProduct).map(productMapper::toListDto);
    }

    /**
     * 해당 상품을 찜한 사용자 리스트 출력
     */
    public List<User> findByProduct(Long referenceID) {
        Product product = productRepository.findByIdOrThrow(referenceID);
        List<WishList> wishLists = wishListRepository.findByProduct(product);
        return wishLists.stream().map(WishList::getUser).toList();
    }

    public List<Long> findByProductLong(Long referenceID) {
        Product product = productRepository.findByIdOrThrow(referenceID);
        List<WishList> wishLists = wishListRepository.findByProduct(product);
        List<Long> wishidList = new ArrayList<>();
        for (WishList wishList : wishLists) {
            wishidList.add(wishList.getUser().getId());
        }
        return wishidList;
    }

}
