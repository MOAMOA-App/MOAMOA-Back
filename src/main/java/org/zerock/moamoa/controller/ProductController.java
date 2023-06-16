package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.domain.DTO.ProductDTO;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.service.ProductService;

import java.io.File;
import java.io.IOException;
import java.util.*;

//RestController -> return 값을 자동으로 json 형식으로 변환해주는 기능 + Controller
@RequiredArgsConstructor
@RestController
//@Controller
public class ProductController {
    private final ProductService productService;

    @GetMapping("/product")
    public Object searchProducts(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) List<String> categories,
            @RequestParam(required = false) List<String> statuses,
            @RequestParam(defaultValue = "createdAt") String orderBy,
            @RequestParam(defaultValue = "DESC") String sortOrder,
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
    @GetMapping("/create")
    public String getCreatePage(){
        return "product";
    }
    @PostMapping("/product")
    public Object Save(
            @RequestParam(defaultValue = "41") Long userId,
            @ModelAttribute("product") Product product,
            @RequestParam("images") MultipartFile[] images){

        Product productTemp = productService.saveProduct(product, userId);

        if (images != null && images.length > 0) {
            String directoryPath = String.format("D:/WorkSpace/%s", productTemp.getId());
            File directory = new File(directoryPath);
            if (!directory.exists()) {
                directory.mkdirs(); // Create the directory if it doesn't exist
            }

            // Process each image
            for (int i = 0; i < images.length; i++) {
                MultipartFile image = images[i];
                if (!image.isEmpty()) {
                    try {
                        // Save the image to the directory
                        String savedImagePath = String.format("%s/image%d.jpg", directoryPath, i);
                        image.transferTo(new File(savedImagePath));
                        System.out.println("이미지가 저장되었습니다.");
                        /*
                            product Image DB 저장 미완성
                         */
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("이미지가 전송되지 않았습니다.");
                }
            }
        } else {
            System.out.println("이미지가 전송되지 않았습니다.");
        }
        Map<String, Long> response = new HashMap<>();
        response.put("id", productTemp.getId());

        return ResponseEntity.ok(response);
    }

    /**게시글 수정하기
     */
    @PutMapping("/product/{pid}")
    public Object UpdateContents(@ModelAttribute("product") Product product){
        return "product";
    }

    /**게시글 상태 변경하기
     * [참여 모집 | 거래 진행 | 거래 완료 ]
     * @param pid 게시글 id
     */
    @PutMapping("/product/status/{pid}")
    public Object UpdateStatus(@PathVariable Long pid, @RequestParam String Status){
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
