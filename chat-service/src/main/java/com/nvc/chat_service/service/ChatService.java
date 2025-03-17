package com.nvc.chat_service.service;

import com.nvc.chat_service.entity.ChatMessage;
import com.nvc.chat_service.entity.Room;
import com.nvc.chat_service.repository.httpclient.FileClient;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatService {
    MongoTemplate mongoTemplate;
    FileClient fileClient;
    SimpMessagingTemplate messagingTemplate;

    public ChatMessage findLatestMessage(String roomId) {
        Query query = new Query(Criteria.where("roomId").is(roomId))
                .collation(Collation.of("vi"))
                .with(Sort.by(Sort.Direction.DESC, "createdAt"))
                .limit(1);

        return mongoTemplate.findOne(query, ChatMessage.class);
    }

    public ChatMessage saveMessage(ChatMessage message) {
        Query query = new Query(Criteria.where("id").is(message.getRoomId()))
                .collation(Collation.of("vi"));
        List<Room> rooms = mongoTemplate.find(query, Room.class);

        if (rooms.isEmpty()) {
            Room newRoom = Room.<Room>builder()
                    .members(List.of(message.getReceiver(), message.getSender()))
                    .createdAt(Instant.now())
                    .build();
            mongoTemplate.save(newRoom);
            message.setRoomId(newRoom.getId());
            messagingTemplate.convertAndSend("/topic/room/" + message.getReceiver(), "");
        }

        message.setCreatedAt(Instant.now());
        mongoTemplate.save(message);
        return message;
    }

    public List<ChatMessage> getMessages(String roomId, int page, int size) {
        Query query = new Query(Criteria.where("roomId").is(roomId))
                .with(Sort.by(Sort.Direction.DESC, "timestamp"))
                .limit(size)
                .collation(Collation.of("vi"))
                .skip((long) page * size);
        return mongoTemplate.find(query, ChatMessage.class);
    }

    public void deleteMessage(String messageId, String userId) {
        Query query = new Query(Criteria.where("_id").is(messageId));
        Update update = new Update().push("deletedBy", userId);
        mongoTemplate.updateFirst(query, update, ChatMessage.class);
    }

    public List<String> uploadImages(List<MultipartFile> images) {
        return fileClient.uploadImages(images, "CHAT_IMAGE");
    }
}
