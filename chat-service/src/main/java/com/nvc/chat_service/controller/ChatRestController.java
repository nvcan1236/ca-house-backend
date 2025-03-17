package com.nvc.chat_service.controller;

import com.nvc.chat_service.dto.ApiResponse;
import com.nvc.chat_service.dto.RoomResponse;
import com.nvc.chat_service.entity.ChatMessage;
import com.nvc.chat_service.entity.Room;
import com.nvc.chat_service.service.ChatService;
import com.nvc.chat_service.service.RoomService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ChatRestController {
    ChatService chatService;
    RoomService roomService;


    @GetMapping("/messages/{roomId}")
    public ApiResponse<List<ChatMessage>> getMessages(@PathVariable String roomId) {
        return ApiResponse.<List<ChatMessage>>builder()
                .result(chatService.getMessages(roomId, 0, 100))
                .build();
    }

    @GetMapping("/room")
    public ApiResponse<List<RoomResponse>> getRooms() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return ApiResponse.<List<RoomResponse>>builder()
                .result(roomService.getRoomsByUser(username))
                .build();
    }


    @PostMapping("/upload")
    public ApiResponse<List<String>> uploadImages(
            @RequestParam(value = "images", required = false) List<MultipartFile> images) {
        return ApiResponse.<List<String>>builder()
                .result(chatService.uploadImages(images))
                .build();
    }
}
