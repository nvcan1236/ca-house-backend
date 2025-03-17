package com.nvc.chat_service.service;

import com.nvc.chat_service.dto.RoomResponse;
import com.nvc.chat_service.entity.ChatMessage;
import com.nvc.chat_service.entity.Room;
import com.nvc.chat_service.repository.ChatRepository;
import com.nvc.chat_service.repository.httpclient.UserClient;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Collation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;

@Service
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RoomService {
    MongoTemplate mongoTemplate;
    UserClient userClient;
    ChatService chatService;

    public List<RoomResponse> getRoomsByUser(String userId) {
        Query query = new Query(Criteria.where("members").in(userId))
                .collation(Collation.of("vi"));
        List<Room> rooms = mongoTemplate.find(query, Room.class);

        return rooms.stream().map(room -> RoomResponse.builder()
                        .id(room.getId())
                        .members(List.of(
                                userClient.getUserById(room.getMembers().getFirst()).getResult(),
                                userClient.getUserById(room.getMembers().getLast()).getResult()
                        ))
                        .lastMessage(chatService.findLatestMessage(room.getId()))
                        .createdAt(room.getCreatedAt())
                        .build())
                .sorted(Comparator.comparing((RoomResponse room) ->
                                room.getLastMessage() != null ? room.getLastMessage().getCreatedAt() : Instant.MIN)
                        .reversed())
                .toList();
    }
}
