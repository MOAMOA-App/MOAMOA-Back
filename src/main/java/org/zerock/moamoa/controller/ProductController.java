package org.zerock.moamoa.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.common.message.OkResponse;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.product.*;
import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.ProductStatus;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.utils.redis.RedisResponse;
import org.zerock.moamoa.utils.redis.SearchRedisUtils;
import org.zerock.moamoa.utils.redis.ViewsRedisUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

//RestController -> return 값을 자동으로 json 형식으로 변환해주는 기능 + Controller
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;
    private final UserRepository userRepository;

    /**
     * 게시글 조회
     *
     * @param keyword  검색어
     * @param category 카테고리
     * @param status   거래 상태 -> 거래 준비 | 거래 진행 | 거래 완효
     * @param search   검색 기준 -> sub 제목 |  descript 내용 | subdesc 제목 + 내용
     * @param order    정렬 기준
     * @param pageNo   페이지 번호
     * @param pageSize 페이지 크기
     */
    @GetMapping("")
    public Page<ProductListResponse> searchProducts(
            @RequestParam(required = false, defaultValue = "") String keyword,
            @RequestParam(required = false) List<Integer> catetory,
            @RequestParam(required = false) List<Integer> status,
            @RequestParam(defaultValue = "subdesc") String search,
            @RequestParam(defaultValue = "recent") String order,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "20") int pageSize
    ) {
        List<ProductStatus> productStatuses = status.stream().map(ProductStatus::fromCode).filter(Objects::nonNull).collect(Collectors.toSet()).stream().toList();
        log.info(productStatuses.toString());
        List<Category> categories = catetory.stream().map(Category::fromCode).filter(Objects::nonNull).collect(Collectors.toSet()).stream().toList();
        log.info(categories.toString());
        // 검색어 Redis 등록
        String[] keywords = keyword.split(" ");
        if (keywords != null && !keywords[0].equals("")) SearchRedisUtils.addSearchKeyword(keywords);
        //
        return productService.search(keywords, categories, productStatuses, search, order, pageNo, pageSize);
    }

    /**
     * 상품 상세 조회
     */
    @GetMapping("{pid}")
    public ProductResponse getById(@PathVariable Long pid, HttpServletRequest request, Authentication auth) {
        String agent = request.getHeader("User-Agent");

        if (auth != null) {
            if (!productService.findAuth(pid, auth.getPrincipal().toString()))
                return productService.findOne(pid);
        }

        ViewsRedisUtils.addViewCount(pid, agent);
        return productService.findOne(pid);
    }

    /**
     * 실시간 검색어 순위 24시간 기준
     */
    @GetMapping("top/{number}")
    public List<RedisResponse> getTopN(@PathVariable int number) {
        return SearchRedisUtils.readTopSearchKeywords(number);
    }

    /**
     * 게시글 생성하기
     */
    @PostMapping("")
    public ProductResponse save(Authentication authentication, @RequestBody ProductSaveRequest request) {
        return productService.saveProduct(request, authentication.getPrincipal().toString());
    }

    /**
     * 게시글 수정하기
     */
    @PutMapping("")
    public ProductResponse updateContents(Authentication authentication, @RequestBody ProductUpdateRequest request) {
        return productService.updateInfo(request, authentication.getPrincipal().toString());
    }

    /**
     * 게시글 상태 변경하기 [참여 모집 | 거래 진행 | 거래 완료 ]
     */
    @PutMapping("status")
    public ProductResponse updateStatus(Authentication authentication, @RequestBody ProductStatusUpdateRequest request) {
        return productService.updateStatus(request, authentication.getPrincipal().toString());
    }

    /**
     * 게시글 삭제하기 : 게시글 진짜 삭제하는게 아니라 Product 활성화 비활성화 처리
     */
    @DeleteMapping("")
    public Object deleteContents(Authentication authentication, @RequestBody ProductStatusUpdateRequest request) {
        productService.remove(request, authentication.getPrincipal().toString());
        return new OkResponse(SuccessMessage.PRODUCT_DELETE).makeAnswer();
    }


}
