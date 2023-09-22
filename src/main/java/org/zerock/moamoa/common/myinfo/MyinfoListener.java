package org.zerock.moamoa.common.myinfo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.notice.NoticeSaveRequest;
import org.zerock.moamoa.domain.DTO.party.PartyResponse;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.service.PartyService;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.service.UserService;
import org.zerock.moamoa.service.WishListService;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class MyinfoListener {
    // 하... 솔직히리스너이렇게쓰는거아닌거같긴한데 먼가짬처리처럼되어버림...
    private final UserService userService;
    private final ProductService productService;
    private final WishListService wishListService;
    private final PartyService partyService;

    @TransactionalEventListener
    @Async
    public Page<ProductResponse> handleMyPost(MyinfoEvent myinfoEvent) {
        User user = userService.findById(myinfoEvent.getUid());
        Pageable itemPage = PageRequest.of(myinfoEvent.getPageNo(), myinfoEvent.getPageSize());
        Page<Product> productPage = productService.findPageByUser(user, itemPage);
        if (productPage.isEmpty()) {
            throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        return productPage.map(product -> productService.findOne(product.getId()));
    }
}