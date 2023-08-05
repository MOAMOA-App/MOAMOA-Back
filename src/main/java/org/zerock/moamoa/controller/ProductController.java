package org.zerock.moamoa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> statuses,
            @RequestParam(defaultValue = "createdAt") String orderBy,
            @RequestParam(defaultValue = "DESC") String sortOrder,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        return productService.search(title, description, categories, statuses, orderBy, sortOrder, pageNo, pageSize);
    }

    @GetMapping("/{pid}")
    public ProductResponse getById(@PathVariable Long pid) {
        return productService.findOne(pid);
    }

    @PostMapping("")
    public ProductResponse Save(
            Authentication authentication,
            @ModelAttribute("product") ProductSaveRequest request,
            @RequestParam(value = "images", required = false) MultipartFile[] images) {

        ProductResponse response = productService.saveProduct(request, images, authentication.getPrincipal().toString());
        return response;
    }

    /**
     * 게시글 수정하기
     */
    @PutMapping("/{pid}")
    public ProductResponse UpdateContents(@PathVariable Long pid,
                                          Authentication authentication,
                                          @Valid @ModelAttribute("product") ProductUpdateRequest product,
                                          @RequestParam("images") MultipartFile[] images) {
        return productService.updateInfo(pid, product, images, authentication.getPrincipal().toString());
    }

    /**
     * 게시글 상태 변경하기 [참여 모집 | 거래 진행 | 거래 완료 ]
     */
    @PutMapping("/{pid}/status")
    public ProductResponse UpdateStatus(@PathVariable Long pid,
                                        Authentication authentication,
                                        @Valid @ModelAttribute("product") ProductStatusUpdateRequest product) {
        return productService.updateStatus(pid, product, authentication.getPrincipal().toString());
    }

    /**
     * 게시글 삭제하기 : 게시글 진짜 삭제하는게 아니라 Product 활성화 비활성화 처리
     */
    @DeleteMapping("{pid}")
    public Object DeleteContents(Authentication authentication, @PathVariable Long pid) {
        productService.remove(pid, authentication.getPrincipal().toString());
        return new OkResponse(SuccessMessage.PRODUCT_DELETE).makeAnswer();
    }

}
