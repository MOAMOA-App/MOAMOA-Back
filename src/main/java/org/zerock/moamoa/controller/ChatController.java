package org.zerock.moamoa.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomRequest;
import org.zerock.moamoa.domain.DTO.chat.ChatRoomResponse;
import org.zerock.moamoa.service.ChatService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("chat/rooms")
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/{pid}")
    public Page<ChatRoomResponse> getByProject(
            @PathVariable Long pid,
            @RequestParam(value = "no", defaultValue = "0") int pageNo,
            @RequestParam(value = "size", defaultValue = "20") int pageSize) {
        return chatService.getPageByProjectId(pid, pageNo, pageSize);
    }

    @GetMapping("/{id}/name")
    public ChatRoomResponse getRoomName(@PathVariable("id") Long roomId) {
        return chatService.findRoomById(roomId);
    }

    @PostMapping("")
    public ChatRoomResponse createRoom(@RequestBody ChatRoomRequest request) {
        return chatService.saveChatRoom(request);
    }
}
