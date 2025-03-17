package com.nvc.chat_service.controller;

import com.nvc.chat_service.entity.ChatMessage;
import com.nvc.chat_service.service.ChatService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.Map;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatController {
    ChatService chatService;
    SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat/send/{roomId}")
    public void sendMessage(@Payload ChatMessage message,
                            @DestinationVariable String roomId
    ) {
        ChatMessage savedMessage = chatService.saveMessage(message);
        messagingTemplate.convertAndSend("/topic/room/" + message.getReceiver(), "");
        messagingTemplate.convertAndSend("/topic/chat/" + roomId, savedMessage);
    }

    @MessageMapping("/chat/delete")
    public void deleteMessage(@Payload Map<String, String> payload) {
        chatService.deleteMessage(payload.get("messageId"), payload.get("userId"));
    }
}
