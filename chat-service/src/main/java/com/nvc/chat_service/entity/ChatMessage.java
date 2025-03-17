package com.nvc.chat_service.entity;

import com.nvc.chat_service.enums.MessageType;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;

@Data
@Builder
@Document(collation = "messages", value = "messages")
public class ChatMessage {
    @Id
    private String id;
    private String roomId;
    private String sender;
    private String receiver;
    private String content;
    private MessageType messageType;
    private String mediaUrl;
    private Instant createdAt;
    private List<String> deletedBy;
}
