package com.nvc.user_service.controller;

import com.nvc.user_service.dto.request.MessageRequest;
import com.nvc.user_service.dto.response.ApiResponse;
import com.nvc.user_service.enums.MessageType;
import com.nvc.user_service.exception.AppException;
import com.nvc.user_service.exception.ErrorCode;
import com.nvc.user_service.repository.httpclient.FileClient;
import com.nvc.user_service.service.FirebaseService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/chat")
@Slf4j
public class ChatController {
    FirebaseService firebaseService;
    FileClient fileClient;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse sendMessage(@RequestParam("content") String content,
                                   @RequestParam("recipient") String recipient,
                                   @RequestParam("type") MessageType type,
                                   @RequestParam(value = "images", required = false) List<MultipartFile> images)
            throws ExecutionException, InterruptedException {
//        try {
//
//
//        } catch (Exception ex) {
//            log.error(ex.getMessage());
//            throw  new AppException(ErrorCode.UNCATEGORIZED_ERROR);
//        }

        List<String> messageContent;
        if(type.equals(MessageType.IMAGE)) {
            messageContent = fileClient.uploadImages(images, "CHAT_IMAGE");
        }
        else {
            messageContent = List.of(content);
        }

        MessageRequest message = MessageRequest.builder()
                .content(messageContent)
                .recipient(recipient)
                .type(type)
                .build();

        firebaseService.pushMessage(message);

        return ApiResponse.builder()
                .message("Message was sent.")
                .build();
    }
}
