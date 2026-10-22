package com.javaweb.controller;

import com.javaweb.entity.UserEntity;
import com.javaweb.service.ChatbotService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/chatbot")
public class ChatbotController {

    private static final int MAX_MESSAGE_LENGTH = 500;

    private final ChatbotService chatbotService;

    public ChatbotController(ChatbotService chatbotService) {
        this.chatbotService = chatbotService;
    }

    @PostMapping(value = "/message", produces = "text/plain;charset=UTF-8")
    @ResponseBody
    public ResponseEntity<String> message(@RequestParam(value = "message", required = false) String message,
                                          HttpSession session) {
        UserEntity currentUser = (UserEntity) session.getAttribute("currentUser");

        if (currentUser == null) {
            return ResponseEntity.status(401)
                    .body("Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại.");
        }

        String safeMessage = message == null ? "" : message.trim();
        if (safeMessage.length() > MAX_MESSAGE_LENGTH) {
            return ResponseEntity.badRequest()
                    .body("Câu hỏi quá dài. Vui lòng nhập tối đa " + MAX_MESSAGE_LENGTH + " ký tự.");
        }

        return ResponseEntity.ok(chatbotService.reply(safeMessage, currentUser));
    }
}
