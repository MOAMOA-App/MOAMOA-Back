package org.zerock.moamoa.common.fixture;

import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.product.ProductSaveRequest;
import org.zerock.moamoa.domain.DTO.product.ProductUpdateRequest;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.ProductStatus;
import org.zerock.moamoa.utils.TimeUtils;

import java.time.Instant;

public class ProductFixture {
    public static final Long TEST_PRODUCT_ID = 1L;
    private static final String TEST_TITLE = "상품 제목";
    private static final String TEST_DESCRIPTION = "상품 설명";
    private static final String TEST_CHOICE_SEND = "직거래";
    private static final String TEST_SELLING_AREA = "경기도 수원시 팔달구 수원역";
    private static final String TEST_DETAIL_AREA = "2층 광장 약국 앞";
    private static final ProductStatus TEST_STATUS = ProductStatus.READY;
    private static final int TEST_SELL_PRICE = 10000;
    private static final int TEST_MAX_COUNT = 10;
    private static final String TEST_CATEGORY = "식품";
    private static final Instant TEST_CREATED_AT = TimeUtils.toInstant("23-12-31 08:20");
    private static final String TEST_FINISHED_AT = "29-12-31 08:20";

    public static Product createProduct() {
        Product product = Product.builder()
                .user(null)
                .category(TEST_CATEGORY)
                .sellingArea(TEST_SELLING_AREA)
                .detailArea(TEST_DETAIL_AREA)
                .title(TEST_TITLE)
                .status(TEST_STATUS)
                .sellPrice(TEST_SELL_PRICE)
                .description(TEST_DESCRIPTION)
                .maxCount(TEST_MAX_COUNT)
                .choiceSend(TEST_CHOICE_SEND)
                .finishedAt(TEST_FINISHED_AT)
                .build();

        product.setCreatedAt(TEST_CREATED_AT);
        return product;
    }

    public static Product createProduct(User testUser) {
        Product product = Product.builder()
                .user(testUser)
                .category(TEST_CATEGORY)
                .sellingArea(TEST_SELLING_AREA)
                .detailArea(TEST_DETAIL_AREA)
                .title(TEST_TITLE)
                .status(TEST_STATUS)
                .sellPrice(TEST_SELL_PRICE)
                .description(TEST_DESCRIPTION)
                .maxCount(TEST_MAX_COUNT)
                .choiceSend(TEST_CHOICE_SEND)
                .finishedAt(TEST_FINISHED_AT)
                .build();

        product.setCreatedAt(TEST_CREATED_AT);
        return product;
    }

    public static final ProductResponse TEST_PRODUCT_RESPONSE = ProductResponse.builder()
            .id(TEST_PRODUCT_ID)
            .user(UserFixture.TEST_USER_RESPONSE)
            .category(Category.fromLabel(TEST_CATEGORY))
            .sellingArea(TEST_SELLING_AREA)
            .detailArea(TEST_DETAIL_AREA)
            .title(TEST_TITLE)
            .status(TEST_STATUS)
            .sellPrice(TEST_SELL_PRICE)
            .description(TEST_DESCRIPTION)
            .maxCount(TEST_MAX_COUNT)
            .choiceSend(TEST_CHOICE_SEND)
            .createdAt(TEST_CREATED_AT)
            .finishedAt(TimeUtils.toInstant(TEST_FINISHED_AT))
            .build();

    public static ProductSaveRequest createProductSaveRequest() {
        return ProductSaveRequest.builder()
                .title(TEST_TITLE)
                .description(TEST_DESCRIPTION)
                .category(TEST_CATEGORY)
                .sellingArea(TEST_SELLING_AREA)
                .detailArea(TEST_DETAIL_AREA)
                .maxCount(TEST_MAX_COUNT)
                .choiceSend(TEST_CHOICE_SEND)
                .finishedAt(TEST_FINISHED_AT)
                .sellPrice(TEST_SELL_PRICE)
                .build();
    }

    public static ProductUpdateRequest createProductUpdateRequest() {
        return ProductUpdateRequest.builder()
                .productId(TEST_PRODUCT_ID)
                .category(TEST_CATEGORY)
                .sellingArea(TEST_SELLING_AREA)
                .detailArea(TEST_DETAIL_AREA)
                .title(TEST_TITLE)
                .status(TEST_STATUS)
                .sellPrice(TEST_SELL_PRICE)
                .description(TEST_DESCRIPTION)
                .viewCount(0)
                .maxCount(TEST_MAX_COUNT)
                .choiceSend(TEST_CHOICE_SEND)
                .finishedAt(TEST_FINISHED_AT)
                .build();
    }
}
