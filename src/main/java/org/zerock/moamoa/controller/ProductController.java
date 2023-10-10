package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.common.message.OkResponse;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.product.ProductSaveRequest;
import org.zerock.moamoa.domain.DTO.product.ProductStatusUpdateRequest;
import org.zerock.moamoa.domain.DTO.product.ProductUpdateRequest;
import org.zerock.moamoa.service.ProductService;

import java.util.List;

//RestController -> return 값을 자동으로 json 형식으로 변환해주는 기능 + Controller
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    @GetMapping("")
    public Page<ProductResponse> searchProducts(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> statuses,
            @RequestParam(defaultValue = "subdesc") String search,
            @RequestParam(defaultValue = "recent") String order,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        String[] keywords = keyword.split(" ");
        return productService.search(keywords, categories, statuses, search, order, pageNo, pageSize);
    }

    @GetMapping("/{pid}")
    public ProductResponse getById(@PathVariable Long pid) {
        return productService.findOne(pid);
    }

    @PostMapping("")
    public ProductResponse Save(Authentication authentication, @RequestBody ProductSaveRequest request) {
        return productService.saveProduct(request, authentication.getPrincipal().toString());
    }

    /**
     * 게시글 수정하기
     */
    @PutMapping("")
    public ProductResponse UpdateContents(Authentication authentication, @RequestBody ProductUpdateRequest request) {
        return productService.updateInfo(request, authentication.getPrincipal().toString());
    }

    /**
     * 게시글 상태 변경하기 [참여 모집 | 거래 진행 | 거래 완료 ]
     */
    @PutMapping("status")
    public ProductResponse UpdateStatus(Authentication authentication, @RequestBody ProductStatusUpdateRequest request) {
        return productService.updateStatus(request, authentication.getPrincipal().toString());
    }

    /**
     * 게시글 삭제하기 : 게시글 진짜 삭제하는게 아니라 Product 활성화 비활성화 처리
     */
    @DeleteMapping("")
    public Object DeleteContents(Authentication authentication, @RequestBody ProductStatusUpdateRequest request) {
        productService.remove(request, authentication.getPrincipal().toString());
        return new OkResponse(SuccessMessage.PRODUCT_DELETE).makeAnswer();
    }

}
