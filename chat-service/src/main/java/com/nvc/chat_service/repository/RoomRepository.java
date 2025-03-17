package com.nvc.chat_service.repository;

import com.nvc.chat_service.entity.Room;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface RoomRepository extends MongoRepository<Room, String> {
    List<Room> findAllByMembersContains(String userId);
    Optional<Room> findById(String id);
}
