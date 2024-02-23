package org.zerock.moamoa.service;

import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.zerock.moamoa.common.exception.AuthException;
import org.zerock.moamoa.common.exception.InvalidValueException;
import org.zerock.moamoa.common.fixture.ProductFixture;
import org.zerock.moamoa.common.fixture.UserFixture;
import org.zerock.moamoa.domain.DTO.product.*;
import org.zerock.moamoa.domain.entity.Product;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.domain.enums.Category;
import org.zerock.moamoa.domain.enums.ProductStatus;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class ProductServiceTest {

    @InjectMocks // 실제 구현체를 주입
    ProductService productService;
    @Mock
    ProductRepository productRepository;
    @Mock
    UserRepository userRepository;
    @Mock
    WishListService wishListService;
    @Mock
    ProductMapper productMapper;
    @Mock
    ApplicationEventPublisher eventPublisher;


    private static final String TEST_USER_NAME = "asdf@naver.com";
    private static final Long pid = ProductFixture.TEST_PRODUCT_ID;
    Product product;
    User user;

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        user = UserFixture.createUser();
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, 1L);

        product = ProductFixture.createProduct(user);

        Field pidField = Product.class.getDeclaredField("id");
        pidField.setAccessible(true);
        pidField.set(product, pid);
    }

    @AfterEach
    void tearDown() {
        // product가 존재하는 경우에만 삭제
        product = null;
    }

    @Test
    void findOne_NonUser() throws NoSuchFieldException, IllegalAccessException {
        // given
        Product productNoUser = ProductFixture.createProduct();

        Field pidField = Product.class.getDeclaredField("id");
        pidField.setAccessible(true);
        pidField.set(productNoUser, pid);

        ProductResponse expectedResponse = ProductMapper.INSTANCE.toDto(productNoUser);

        // mocking
        when(productRepository.findByIdOrThrow(pid)).thenReturn(productNoUser);
        when(productMapper.toDto(any(Product.class))).thenReturn(ProductFixture.TEST_PRODUCT_RESPONSE);

        // when
        ProductResponse actualResponse = productService.findOne(pid, null);

        // then
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertFalse(actualResponse.isHeart()); // 비회원이므로 찜 여부는 항상 false여야 합니다.
    }

    @Test
    void findOne_User_NoWishList() {
        // given
        ProductResponse expectedResponse = ProductMapper.INSTANCE.toDto(product);

        // mocking
        when(productRepository.findByIdOrThrow(pid)).thenReturn(product);
        when(userRepository.findByEmailOrThrow(TEST_USER_NAME)).thenReturn(user);
        when(productMapper.toDto(any(Product.class))).thenReturn(ProductFixture.TEST_PRODUCT_RESPONSE);
        when(wishListService.isExist(any(User.class), any(Product.class))).thenReturn(false);

        // when
        ProductResponse actualResponse = productService.findOne(pid, TEST_USER_NAME);

        // then
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertFalse(actualResponse.isHeart()); // 회원이지만 찜하지 않은 경우 찜 여부는 false여야 합니다.
    }

    @Test
    void findOne_User_WithWishList() {
        // given
        ProductResponse expectedResponse = ProductMapper.INSTANCE.toDto(product);

        // mocking
        when(productRepository.findByIdOrThrow(pid)).thenReturn(product);
        when(userRepository.findByEmailOrThrow(TEST_USER_NAME)).thenReturn(user);
        when(productMapper.toDto(any(Product.class))).thenReturn(ProductFixture.TEST_PRODUCT_RESPONSE);
        when(wishListService.isExist(any(User.class), any(Product.class))).thenReturn(true);

        // when
        ProductResponse actualResponse = productService.findOne(pid, TEST_USER_NAME);

        // then
        assertEquals(expectedResponse.getId(), actualResponse.getId());
        assertTrue(actualResponse.isHeart()); // 회원이 찜한 경우 찜 여부는 true여야 합니다.
    }

    @Test
    void findAuth_Fail() throws NoSuchFieldException, IllegalAccessException {
        //given
        String otherUser = "otherUser";
        User other = UserFixture.createUser();
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, 2L);

        when(userRepository.findByEmailOrThrow(otherUser)).thenReturn(other);
        when(productRepository.findByIdOrThrow(pid)).thenReturn(product);

        //when
        boolean result = productService.findAuth(pid, otherUser);

        //then
        assertFalse(result);
    }

    @Test
    void findAuth_Success() {
        //given
        when(userRepository.findByEmailOrThrow(TEST_USER_NAME)).thenReturn(user);
        when(productRepository.findByIdOrThrow(pid)).thenReturn(product);

        //when
        boolean result = productService.findAuth(pid, TEST_USER_NAME);

        //then
        assertFalse(result);
    }

    @Test
    void testSaveProduct_checkFail() {
        //given
        ProductSaveRequest saveRequest = ProductFixture.createProductSaveRequest();
        saveRequest.setSellPrice(-1000);

        when(userRepository.findByEmailOrThrow(TEST_USER_NAME)).thenReturn(user);

        //when & then
        assertThrows(InvalidValueException.class, () -> productService.saveProduct(saveRequest, TEST_USER_NAME));
    }

    @Test
    void testSaveProduct() {
        //given
        ProductSaveRequest saveRequest = ProductFixture.createProductSaveRequest();
        Product unsavedProduct = ProductFixture.createProduct(user);

        ProductResponse expectedResponse = ProductMapper.INSTANCE.toDto(product);

        when(userRepository.findByEmailOrThrow(TEST_USER_NAME)).thenReturn(user);
        when(productMapper.toEntity(saveRequest)).thenReturn(unsavedProduct);
        when(productRepository.save(product)).thenReturn(product);
        when(productMapper.toDto(product)).thenReturn(ProductFixture.TEST_PRODUCT_RESPONSE);

        //when
        ProductResponse actualResponse = productService.saveProduct(saveRequest, TEST_USER_NAME);
        //then
        assertEquals(actualResponse, expectedResponse);
    }

    @Test
    void remove() {
        //given
        when(userRepository.findByEmailOrThrow(TEST_USER_NAME)).thenReturn(user);
        when(productRepository.findByIdOrThrow(pid)).thenReturn(product);

        //when
        boolean isDeleted = productService.remove(pid, TEST_USER_NAME);

        //then
        assertFalse(product.getActivate());
        assertNotNull(product.getDeletedAt());
        assertTrue(isDeleted);
    }

    @Test
    void updateInfo() {
        //given
        String newTitle = "변경된 제목";
        int newMaxCount = 100;
        ProductUpdateRequest updateRequest = ProductFixture.createProductUpdateRequest();
        updateRequest.setTitle(newTitle);
        updateRequest.setMaxCount(newMaxCount);

        when(userRepository.findByEmailOrThrow(TEST_USER_NAME)).thenReturn(user);
        when(productRepository.findByIdOrThrow(pid)).thenReturn(product);

        when(productMapper.toDto(any(Product.class))).thenAnswer(invocation -> {
            Product inputProduct = invocation.getArgument(0);
            return ProductMapper.INSTANCE.toDto(inputProduct);
        });

        //when
        ProductResponse actualResponse = productService.updateInfo(updateRequest, TEST_USER_NAME);

        //then
        assertEquals(actualResponse.getTitle(), newTitle);
        assertEquals(actualResponse.getMaxCount(), newMaxCount);
    }

    @Test
    void updateStatus() {
        //given
        ProductStatus newStatue = ProductStatus.IN_PROGRESS;
        ProductStatusUpdateRequest request = ProductStatusUpdateRequest.builder()
                .productId(pid)
                .status(newStatue)
                .build();

        when(userRepository.findByEmailOrThrow(TEST_USER_NAME)).thenReturn(user);
        when(productRepository.findByIdOrThrow(pid)).thenReturn(product);
        when(productMapper.toDto(any(Product.class))).thenAnswer(invocation -> {
            Product inputProduct = invocation.getArgument(0);
            return ProductMapper.INSTANCE.toDto(inputProduct);
        });

        //when
        ProductResponse actualResponse = productService.updateStatus(request, TEST_USER_NAME);

        //then
        assertEquals(actualResponse.getStatus(), newStatue);
        assertEquals(actualResponse.getId(), pid);

    }

    @Test
        // TODO 미완성
    void search() {
        //given
        String[] keywords = {"제"};
        List<Category> categoryList = new ArrayList<>();
        categoryList.add(Category.FOOD);
        List<ProductStatus> statuses = new ArrayList<>();
        statuses.add(ProductStatus.READY);
        String search = "sub";      // 검색 기준
        String order = "recent";   // 정렬 기준
        int pageNo = 0;         // 페이지 번호
        int pageSize = 30;      //페이지 사이즈

        //mocking
        when(productRepository.findAll(any(Specification.class), any(Pageable.class))).thenAnswer(inv -> {
            Specification<Product> spec = inv.getArgument(0);
            Pageable pageable = inv.getArgument(0);
            return productRepository.findAll(spec, pageable);
        });
        when(wishListService.isExist(any(User.class), any(Product.class))).thenReturn(true);
        when(productMapper.toListDto(any(Product.class))).thenAnswer(invocation -> {
            Product inputProduct = invocation.getArgument(0);
            return ProductMapper.INSTANCE.toListDto(inputProduct);
        });

        //when
        Page<ProductListResponse> responses = productService.search(keywords, categoryList, statuses,
                search, order, pageNo, pageSize, TEST_USER_NAME);

        //then
    }

    @Test
    void findPageByUser() {
        //given

        //when

        //then
    }


    @Test
    @DisplayName("제품 권한 확인 - 권한 실패")
    void checkAuthFailTest() throws IllegalAccessException, NoSuchFieldException {
        // given
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        User owner = UserFixture.createUser();
        User other = UserFixture.createUser();
        idField.set(owner, 1L);
        idField.set(other, 2L);

        Product product = ProductFixture.createProduct(owner);

        // when & then
        // 예외가 발생해야 함: 소유자와 다른 사용자가 인증을 시도했기 때문에 권한 검사 실패 예외가 발생해야 함
        Assertions.assertNotEquals(owner.getId(), other.getId());
        assertThrows(AuthException.class, () -> productService.checkAuth(product, other));
    }

    @Test
    @DisplayName("제품 권한 확인 - 권한 성공")
    void checkAuthSuccessTest() {
        // given
        Product product = ProductFixture.createProduct(user);

        // when & then
        // 예외가 발생하지 않아야 함: 소유자와 일치하는 사용자가 인증을 시도했기 때문에 권한 검사 실패 예외가 발생하지 않아야 함
        assertDoesNotThrow(() -> productService.checkAuth(product, user));
    }


}