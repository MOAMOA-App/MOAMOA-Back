package org.zerock.moamoa.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
class ChatTestController {

    @GetMapping("chat")
    public String index() {
        return "chat-lobby";
    }

    @GetMapping("chat/{roomId}")
    public String enterRoom() {
        return "chat-room";
    }
}
