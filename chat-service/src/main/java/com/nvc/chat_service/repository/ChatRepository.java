package com.nvc.chat_service.repository;

import com.nvc.chat_service.entity.ChatMessage;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ChatRepository extends MongoRepository<ChatMessage, String> {
    List<ChatMessage> findAllByRoomId(String roomId);

    @Query(value = "{ 'members': { $in: [?0] } }", collation = "{ 'locale': 'vi' }")
    ChatMessage findTopByRoomIdOrderByCreatedAtDesc(String roomId);
}
