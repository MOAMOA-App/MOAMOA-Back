package org.zerock.moamoa.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.ProductDTO;
import org.zerock.moamoa.domain.DTO.UserDTO;
import org.zerock.moamoa.domain.SignupRequest;
import org.zerock.moamoa.domain.entity.User;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.service.EmailService;
import org.zerock.moamoa.service.ProductService;
import org.zerock.moamoa.service.UserService;
import org.zerock.moamoa.service.WishListService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
//@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private EmailService emailService;

    @Autowired
    private final ProductService productService;

    @Autowired
    private final WishListService wishListService;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserService userService, EmailService emailService, ProductService productService, WishListService wishListService, UserRepository userRepository) {
        this.userService = userService;
        this.emailService = emailService;
        this.productService = productService;
        this.wishListService = wishListService;
        this.userRepository = userRepository;
    }

    // 일단 걍 ResponseEntity로 작성했는데 나중에 리다이렉션되도록 바꿔야할지도... (RedirectView)
    // ResponseEntity: httpentity를 상속(HttpStatus, HttpHeaders HttpBody 포함,
    // 결과 데이터와 HTTP 상태 코드를 직접 제어할 수 있는 클래스. 사용자의  HttpRequest에 대한 응답 데이터가 포함.
    // Client의 요청에 대한 응답을 한번 더 감싸는 역할.
    // 에러 코드와 같은 HTTP상태 코드를 전송하고 싶은 데이터와 함께 전송할 수 있기 때문에 좀 더 세밀한 제어가 필요한 경우 사용
    // ResponseEntity 사용시 주의점: void 처리할 때 Objects 로 데이터 타입을 명시하는 게 가독성, 에러, 유지보수 측면에 좋음

    /**
     * 로그인
     * @param email
     * @param password
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestParam String email, @RequestParam String password) {
        try {
            // 주어진 이메일로 데이터베이스에서 사용자 찾음
            Optional<User> optionalUser = userRepository.findByEmail(email);

            if (optionalUser.isPresent()) {
                User user = optionalUser.get();

                // UserService의 메서드를 사용하여 비밀번호 확인
                boolean isPasswordCorrect = userService.checkPassword(password, user.getPassword());

                if (isPasswordCorrect) {
                    // 비밀번호가 일치하는 경우 로그인 성공
                    return ResponseEntity.ok("로그인 성공");
                } else {
                    // 비밀번호가 일치하지 않는 경우
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("비밀번호가 일치하지 않습니다");
                }
            } else {
                // 이메일이 존재하지 않는 경우
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("존재하지 않는 이메일입니다");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("로그인에 실패했습니다");
        }
    }

    @GetMapping("/user/signup")
    public ResponseEntity<String> getSignUpPage() {
        return ResponseEntity.ok("회원가입 페이지");
    }

    /**
     * 회원가입
     * @param request
     * @return
     */
    @PostMapping("/user/signup")
    public ResponseEntity<String> signUp(@RequestParam() SignupRequest request) {
        // signUp 메서드는 @RequestBody 어노테이션을 사용하여 클라이언트로부터 전달된 JSON 데이터를 User 객체로 매핑
        // 이렇게 생성된 User 객체를 UserService의 saveUser 메서드를 호출하여 저장
        try {
            User user = new User();
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setName(request.getName());
            user.setJoinDate(LocalDateTime.now());
            userService.saveUser(user);

            return ResponseEntity.ok("회원가입 성공");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("회원가입 실패");
        }
    }

    /**
     * 이메일 인증번호 보냄
     * @param email
     * @return
     * @throws Exception
     */
    @PostMapping("/user/signup/emailConfirm")
    public String emailConfirm(@RequestParam String email) throws Exception {
        return emailService.sendSimpleMessage(email);
    }

    /**
     * 회원탈퇴
     * @param id
     * @return
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        userService.removeUser(id);
        return ResponseEntity.ok("User with ID " + id + " has been deleted.");
    }


    /**
     * 프로필 조회 (UserDTO 사용해봄)
     * @param id
     * @return
     */
    public ResponseEntity<UserDTO> getProfile(@PathVariable Long id) {
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            UserDTO userDTO = new UserDTO(user);
            return ResponseEntity.ok(userDTO); // 성공 상태 코드(200 OK)와 함께 사용자 정보를 클라이언트에게 반환
        } else {
            return ResponseEntity.notFound().build();
        }
    }

//    @GetMapping("/profile/{id}")
//    public ResponseEntity<User> getProfile(@PathVariable Long id) {
//        Optional<User> optionalUser = userService.findById(id);
//        if (optionalUser.isPresent()) {
//            User user = optionalUser.get();
//            return ResponseEntity.ok(user); // 성공 상태 코드(200 OK)와 함께 사용자 정보를 클라이언트에게 반환
//        } else {
//            return ResponseEntity.notFound().build();
//        }
//    }


    /**
     * 프로필 수정
     * @param id
     * @param updatedUser
     * @return
     */
    @PutMapping("/profile/{id}/edit") // api명세 참고
    public ResponseEntity<User> updateProfile(@PathVariable Long id, @RequestBody User updatedUser) {
        // @PathVariable을 사용하여 URL에서 id를 추출
        // @RequestBody를 사용하여 요청 본문의 JSON을 User 객체로 매핑

        // userService.findById()로 해당 id의 사용자 찾음, 수정할 필드들 업데이트
        Optional<User> optionalUser = userService.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setNick(updatedUser.getNick());
            user.setProfImg(updatedUser.getProfImg());
            user.setPassword(updatedUser.getPassword());
            user.setAddress(updatedUser.getAddress());
            user.setDetailAddress(updatedUser.getDetailAddress());

            // userService.saveUser()를 호출하여 수정된 사용자 정보를 저장
            User updatedUserProfile = userService.saveUser(user);

            // 수정된 프로필 정보를 클라이언트에게 반환하기 위해 ResponseEntity.ok() 사용
            return ResponseEntity.ok(updatedUserProfile);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * 내가 작성한 게시글 불러옴
     * @param userId
     * @return
     */
    @GetMapping("/myinfo/userpost/{id}")
    public List<ProductDTO> getUserPosts(@PathVariable("id") Long userId) {
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다. id=" + userId));

        return productService.getProductsByUserId(userId);
    }

    /**
     * 내가 참여한 게시글 불러옴
     * @param id
     * @return
     */
    @GetMapping("/myinfo/userparty/{id}")
    public ResponseEntity<List<UserDTO>> getMyUserParty(@PathVariable("id") Long id) {
        try {
            List<UserDTO> userDTOList = userService.getMyUserParty(id);
            return ResponseEntity.ok(userDTOList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * 내가 찜한 게시글 불러옴
     * @param userId
     * @return
     */
    @GetMapping("/myinfo/userheart/{id}")
    public List<Long> getUserHeartProducts(@PathVariable("id") Long userId) {
        // 위시리스트에 있는 상품들 불러옴, 가져온 상품 목록을 ProductDTO 객체로 변환하여 products 리스트에 저장
        List<ProductDTO> products = productService.getProductsByWishListUserId(userId);

        // products에서 상품id 추출-> map(ProductDTO::getId) 통해 ID 목록 생성
        // 생성된 ID 목록을 Collectors.toList() 사용해 productIds 리스트에 저장
        List<Long> productIds = products.stream()
                .map(ProductDTO::getId)
                .collect(Collectors.toList());

        // 사용자의 위시리스트에 있는 상품의 ID 목록을 클라이언트에게 보냄
        return productIds;
    }

}
