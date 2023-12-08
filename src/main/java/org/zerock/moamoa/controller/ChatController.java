package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomRequest;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomResponse;
import org.zerock.moamoa.repository.ChatRoomRepository;
import org.zerock.moamoa.repository.ProductRepository;
import org.zerock.moamoa.repository.UserRepository;
import org.zerock.moamoa.service.ChatService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatController {
    private final ChatService chatService;
    private final ChatRoomRepository chatRoomRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    // 유저기준 채팅방 불러옴 (채팅 이모지 클릭하면 나오는 화면)
    @GetMapping("")
    public Page<ChatRoomResponse> getByUser(
            Authentication auth,
            @RequestParam(value = "no", defaultValue = "0") int pageNo,
            @RequestParam(value = "size", defaultValue = "20") int pageSize) {
        return chatService.getPageByUserId(auth.getPrincipal().toString(), pageNo, pageSize);
    }
    //채팅방 조회
//    @GetMapping("/room")
//    public void getRoom(String roomId, Model model, HttpSession session){
//        //        Long memberId = (Long) session.getAttribute(LOGIN_ID);
//        //        String memberProfileName = ms.findById(memberId).getMemberProfileName();
//        //        model.addAttribute("memberProfileName",memberProfileName);
//        log.info("# get Chat Room, roomID : " + roomId);
//
//        model.addAttribute("room", cs.findRoomById(roomId));
//    }

    @GetMapping("/{pid}")
    public Page<ChatRoomResponse> getByProject(
            @PathVariable Long pid,
            Authentication auth,
            @RequestParam(value = "no", defaultValue = "0") int pageNo,
            @RequestParam(value = "size", defaultValue = "20") int pageSize) {
        return chatService.getPageByProjectId(pid, auth.getPrincipal().toString(), pageNo, pageSize);
    }

    @GetMapping("/{id}/name")
    public ChatRoomResponse getRoomName(@PathVariable("id") Long roomId, Authentication auth) {
        return chatService.findRoomById(auth.getPrincipal().toString(), roomId);
    }

    @PostMapping("/create")
    public ResultResponse createRoom(@RequestBody ChatRoomRequest req) {
        // 채팅방 만들어지는 시점: 참여자 목록에서 채팅 메시지 보낼때 -> pid/sellerid/uid 중복있는지 검사 (3가지 동시에),
        // 중복 없으면 방 만들기: 채팅방 초대(ChatRoom에 추가) + 메시지 보내기(WebSocketController)
        if (chatRoomRepository.existsByProductIdAndUserId(
                productRepository.findById(req.getProductId()),
                userRepository.findById(req.getUserId()))
        ) return ResultResponse.toDto("ALREADY_EXIST");
        chatService.saveChatRoom(req);
        return ResultResponse.toDto("OK");
    }
}
