package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.AnnounceDTO;
import org.zerock.moamoa.domain.DTO.ProductDTO;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.service.ProductService;


import javax.print.DocFlavor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//RestController -> return 값을 자동으로 json 형식으로 변환해주는 기능 + Controller
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;

    /**게시글 전체 리스트 받기
     * @param limit 페이지 당 게시글 개수
     * @param skip  현재 페이지
     * ex) limit = 30, skip 6 -> 출력 = 180 ~ 209번 게시글 출력
     */
//    @GetMapping("/product")
//    public ResponseEntity<Map<String, List<ProductDTO>>> GetList(@RequestParam(value = "limit", defaultValue = "0") int limit,
//                                 @RequestParam(value = "skip",  defaultValue = "0") int skip){
//        Map<String, List<ProductDTO>> response = new HashMap<>();
//        response.put("product", productService.getList());
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/product")
    public Object searchProducts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> statuses,
            @RequestParam(required = false) String orderBy,
            @RequestParam(required = false) String sortOrder,
            @RequestParam(defaultValue = "0") int pageNo,
            @RequestParam(defaultValue = "10") int pageSize
    ) {
        return productService.searchProducts(title, description, categories, statuses, orderBy, sortOrder, pageNo, pageSize);
    }

    /**게시글 상세 받기
     * @param pid 게시글 id
     */
    @GetMapping("/product/{pid}")
    public Object getById(@PathVariable Long pid) {
        ProductDTO productDTO = productService.getById(pid);

        if (productDTO.getId() == null) {
            return ResponseEntity.noContent().build();
        }else{
            Map<String, ProductDTO> response = new HashMap<>();
            response.put("product", productDTO);

            return ResponseEntity.ok(response);
        }
    }

    /**
     * 게시글 생성하기
     */
    @PostMapping("/product")
    public String Save(){
        //productService.saveProduct();
        return "product";
    }

    /**게시글 수정하기
     * @param pid 게시글 id
     */
    @PutMapping("/product/{pid}")
    public Object UpdateContents(@PathVariable Long pid){
        return "product";
    }

    /**게시글 상태 변경하기
     * [참여 모집 | 거래 진행 | 거래 완료 ]
     * @param pid 게시글 id
     */
    @PutMapping("/product/status/{pid}")
    public Object UpdateStatus(@PathVariable Long pid){
        return "product";
    }

    /**게시글 삭제하기
     * @param pid 게시글 id
     * return 삭제 성공 여부 message 출력
     */
    @DeleteMapping("/product/{pid}")
    public Object DeleteContents(@PathVariable Long pid){
        String message;
        if(productService.removeProduct(pid)){
            message = "삭제되었습니다.";
            return ResponseEntity.ok(message);
        }else{
            message = "존재하지 않는 게시글입니다.";
            return ResponseEntity.badRequest().body(message);
        }

    }
}
