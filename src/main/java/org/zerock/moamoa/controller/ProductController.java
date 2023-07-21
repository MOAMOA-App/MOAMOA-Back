package org.zerock.moamoa.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.common.file.ImageService;
import org.zerock.moamoa.common.message.OkResponse;
import org.zerock.moamoa.common.message.SuccessMessage;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.product.ProductSaveRequest;
import org.zerock.moamoa.domain.DTO.product.ProductStatusUpdateRequest;
import org.zerock.moamoa.domain.DTO.product.ProductUpdateRequest;
import org.zerock.moamoa.service.ProductService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.Instant;
import java.util.List;

//RestController -> return 값을 자동으로 json 형식으로 변환해주는 기능 + Controller
@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/product")
public class ProductController {
	private final ProductService productService;
	private final ImageService imageService = new ImageService("/product/");

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
		@Valid @ModelAttribute("product") ProductSaveRequest request,
		@RequestParam("images") MultipartFile[] images) {
		request.setFinishedAt(Instant.now());
		log.info(request.toString());
		ProductResponse response = productService.saveProduct(request, images);
		return response;
	}

	/**
	 * 게시글 수정하기 : TODO 수정 시 기존의 이미지 삭제 추가할 것
	 */
	@PutMapping("/{pid}")
	public ProductResponse UpdateContents(@PathVariable Long pid,
		@Valid @ModelAttribute("product") ProductUpdateRequest product,
		@RequestParam("images") MultipartFile[] images) {
		return productService.updateInfo(product, images);
	}

	/**
	 * 게시글 상태 변경하기 [참여 모집 | 거래 진행 | 거래 완료 ]
	 */
	@PutMapping("/{pid}/status")
	public ProductResponse UpdateStatus(@PathVariable Long pid,
		@Valid @ModelAttribute("product") ProductStatusUpdateRequest product) {
		return productService.updateStatus(product);
	}

	/**
	 * 게시글 삭제하기 : 게시글 진짜 삭제하는게 아니라 Product 활성화 비활성화 처리
	 */
	@DeleteMapping("{pid}")
	public Object DeleteContents(@PathVariable Long pid) {
		productService.remove(pid);
		return new OkResponse(SuccessMessage.PRODUCT_DELETE).makeAnswer();
	}

}
