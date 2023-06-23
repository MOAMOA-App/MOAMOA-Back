package org.zerock.moamoa.controller;

import groovy.util.logging.Slf4j;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.zerock.moamoa.component.ImageService;
import org.zerock.moamoa.domain.DTO.ProductDTO;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.service.ProductService;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.*;
import java.util.List;

//RestController -> return 값을 자동으로 json 형식으로 변환해주는 기능 + Controller
@RequiredArgsConstructor
@RestController
public class ProductController {
    private final ProductService productService;
    private final ImageService imageService;

    @GetMapping("/product")
    public Page<ProductDTO> searchProducts(
            @RequestParam(required = false) String       title,
            @RequestParam(required = false) String       description,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> statuses,
            @RequestParam(defaultValue = "createdAt") String orderBy,
            @RequestParam(defaultValue = "DESC")      String sortOrder,
            @RequestParam(defaultValue = "0")         int    pageNo,
            @RequestParam(defaultValue = "20")        int    pageSize
    ) {
        return productService.searchProducts(title, description, categories, statuses, orderBy, sortOrder, pageNo, pageSize);
    }

    /**게시글 상세 받기
     * @param pid 게시글 id
     */
    @GetMapping("/product/{pid}")
    public ProductDTO getById(@PathVariable Long pid) {
        ProductDTO productDTO = productService.getById(pid);

        if (productDTO.getId() != null) {
            return productDTO;
        }else throw new ResponseStatusException(HttpStatus.NO_CONTENT);
    }

    /**
     * 게시글 생성하기
     */

    @PostMapping("/product")
    public Long Save(
            @RequestParam(defaultValue = "41") Long userId,
            @ModelAttribute("product") Product product,
            @RequestParam("images") MultipartFile[] images){

        product.setCountImage(images.length);
        //테스트용 코드
        product.setFinishedAt(Instant.now());
        //-----------
        Product productTemp = productService.saveProduct(product, userId);
        if(imageService.saveProductImage(images, productTemp.getId()))
            return productTemp.getId();
        else
            throw new ResponseStatusException(HttpStatus.NO_CONTENT,"이미지 저장에 실패했습니다.");
    }

    /**
     * 게시글 수정하기
     * kse -> 수정 시 기존의 이미지 삭제 추가할 것
     */
    @PutMapping("/product/{pid}")
    public String UpdateContents(@PathVariable Long pid,
                                 @ModelAttribute("product") Product product,
                                 @RequestParam("images") MultipartFile[] images ){

        product.setCountImage(images.length);
        //테스트용 코드
        product.setFinishedAt(Instant.now());
        //----------
        if(imageService.saveProductImage(images, pid)){
            if(productService.updateContents(product))
                return "게시글을 수정했습니다";
        }
        else
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"게시글 수정에 실패했습니다.");
        return "";
    }

    /**게시글 상태 변경하기
     * [참여 모집 | 거래 진행 | 거래 완료 ]
     * @param pid 게시글 id
     */
    @PutMapping("/product/status/{pid}")
    public Object UpdateStatus(@PathVariable Long pid, @RequestParam String status){
        if(productService.updateStatus(pid, status))
            return "게시글의 거래상태를 변경했습니다";
        else throw new ResponseStatusException(HttpStatus.BAD_REQUEST,"게시글의 거래상태를 변경하는데에 실패했습니다.");
    }

    /**게시글 삭제하기
     * @param pid 게시글 id
     * return 삭제 성공 여부 message 출력
     * 게시글 진짜 삭제하는게 아니라 Product 활성화 비활성화 처리를 위해 속성 추가할 것
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
