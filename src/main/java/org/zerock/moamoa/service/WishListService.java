package org.zerock.moamoa.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.entity.WishList;
import org.zerock.moamoa.repository.WishListRepository;

@Service
public class WishListService {
	private final WishListRepository wishListRepository;
	private final ProductService productService;
	private final UserService userService;

	@Autowired
	public WishListService(WishListRepository wishListRepository, ProductService productService,
		UserService userService) {
		this.wishListRepository = wishListRepository;
		this.productService = productService;
		this.userService = userService;
	}

	@Transactional
	public WishList findById(Long id) {
		Optional<WishList> wishList = wishListRepository.findById(id);
		if (wishList.isPresent())
			return wishList.get();
		else
			throw new RuntimeException(new RuntimeException("해당 id(" + id + ")의 WishList를 찾을 수 없습니다."));
	}

	@Transactional
	public List<WishList> findAll() {
		return this.wishListRepository.findAll();
	}

	@Transactional
	public WishList saveWish(Long user_id, Long product_id) {
		WishList wishList = new WishList();
		User user = userService.findById(user_id);
		Product product = productService.findById(product_id);
		if (product == null) {
			throw new IllegalArgumentException("해당 상품이 없습니다. id=" + product_id);
		}
		wishList.setUserId(user);
		wishList.setProductId(product);
		user.addWishList(wishList); // user의 wishlists에 정보 추가
		return wishListRepository.save(wishList);
	}

	@Transactional
	public void removeWish(Long id) {
		WishList wishList = wishListRepository.findById(id)
			.orElseThrow(() -> new IllegalArgumentException("해당 위시리스트가 없습니다. id=" + id));

		// User의 removeWishList를 호출해서 WishList 제거하는 코드 추가해봄
		User user = wishList.getUserId();
		if (user != null) {
			user.removeWishList(wishList);
			userService.saveUser(user);
		}

		this.wishListRepository.delete(wishList);
	}

}
