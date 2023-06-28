package org.zerock.moamoa.controller;

import java.time.Instant;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.zerock.moamoa.api.file.ImageService;
import org.zerock.moamoa.common.exception.EntityNotFoundException;
import org.zerock.moamoa.common.exception.ErrorCode;
import org.zerock.moamoa.domain.DTO.product.ProductResponse;
import org.zerock.moamoa.domain.DTO.product.ProductSaveRequest;
import org.zerock.moamoa.domain.DTO.product.ProductStatusUpdateRequest;
import org.zerock.moamoa.domain.DTO.product.ProductUpdateRequest;
import org.zerock.moamoa.service.ProductService;

import lombok.RequiredArgsConstructor;

//RestController -> return 값을 자동으로 json 형식으로 변환해주는 기능 + Controller
@RequiredArgsConstructor
@RestController
public class ProductController {
	private final ProductService productService;
	private final ImageService imageService = new ImageService("/product/");

	@GetMapping("/product")
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

	@GetMapping("/product/{pid}")
	public ProductResponse getById(@PathVariable Long pid) {
		return productService.findOne(pid);
	}

	@PostMapping("/product")
	public ProductResponse Save(
		@RequestParam(defaultValue = "41") Long userId,
		@ModelAttribute("product") ProductSaveRequest product,
		@RequestParam("images") MultipartFile[] images) {

		product.setCountImage(images.length);
		product.setFinishedAt(Instant.now());
		ProductResponse response = productService.saveProduct(product, userId);
		imageService.saveImages(images, response.getId());

		return response;
	}

	/**
	 * 게시글 수정하기
	 * kse -> 수정 시 기존의 이미지 삭제 추가할 것
	 */
	@PutMapping("/product/{pid}")
	public ProductResponse UpdateContents(@PathVariable Long pid,
		@ModelAttribute("product") ProductUpdateRequest product,
		@RequestParam("images") MultipartFile[] images) {

		product.setCountImage(images.length);
		imageService.saveImages(images, pid);

		return productService.updateInfo(product);
	}

	/**
	 * 게시글 상태 변경하기
	 * [참여 모집 | 거래 진행 | 거래 완료 ]
	 */
	@PutMapping("/product/status/{pid}")
	public ProductResponse UpdateStatus(@PathVariable Long pid,
		@ModelAttribute("product") ProductStatusUpdateRequest product) {
		return productService.updateStatus(product);
	}

	/**
	 * 게시글 삭제하기
	 *
	 * @param pid 게시글 id
	 *            return 삭제 성공 여부 message 출력
	 *            게시글 진짜 삭제하는게 아니라 Product 활성화 비활성화 처리를 위해 속성 추가할 것
	 */
	@DeleteMapping("/product/{pid}")
	public Object DeleteContents(@PathVariable Long pid) {
		if (productService.remove(pid))
			return ResponseEntity.ok();
		else
			throw new EntityNotFoundException(ErrorCode.PRODUCT_NOT_FOUND);
	}
}
