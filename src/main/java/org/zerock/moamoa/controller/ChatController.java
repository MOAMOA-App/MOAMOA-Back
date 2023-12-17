package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.ResultResponse;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomRequest;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomResponse;
import org.zerock.moamoa.service.ChatService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/chat/rooms")
public class ChatController {
    private final ChatService chatService;

    /** 유저 기준 채팅방 조회 */
    @GetMapping("")
    public Page<ChatRoomResponse> getByUser(
            Authentication auth,
            @RequestParam(value = "no", defaultValue = "0") int pageNo,
            @RequestParam(value = "size", defaultValue = "20") int pageSize) {
        return chatService.getPageByUserId(auth.getPrincipal().toString(), pageNo, pageSize);
    }

    /** 상품 기준 채팅방 조회*/
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

    /** 채팅방 생성
     * 생성 시점: 참여자 목록에서 채팅 메시지 보낼때 중복검사 후 방 생성
     * 채팅방 초대(ChatRoom에 추가) + 메시지 보내기(WebSocketController)
     */
    @PostMapping("/create")
    public ResultResponse createRoom(@RequestBody ChatRoomRequest req) {
        return chatService.saveChatRoom(req);
    }
}
