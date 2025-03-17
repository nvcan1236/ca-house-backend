package com.nvc.chat_service.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;
import java.util.List;
@Builder
@Data
@Document(collation = "rooms", value = "rooms")
public class Room {
    @Id
    private String id;
    List<String> members;
    private Instant createdAt;
}
