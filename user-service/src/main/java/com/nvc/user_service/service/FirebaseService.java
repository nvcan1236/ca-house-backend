package com.nvc.user_service.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.Timestamp;
import com.google.cloud.firestore.*;
import com.nvc.user_service.dto.request.MessageRequest;
import com.nvc.user_service.dto.response.ChatRoomResponse;
import com.nvc.user_service.dto.response.ChatUserResponse;
import com.nvc.user_service.dto.response.MessageResponse;
import com.nvc.user_service.entity.User;
import com.nvc.user_service.enums.UserRole;
import com.nvc.user_service.exception.AppException;
import com.nvc.user_service.exception.ErrorCode;
import com.nvc.user_service.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FirebaseService {
    Firestore firestore;
    UserRepository userRepository;

    public boolean checkUser(String username) throws ExecutionException, InterruptedException {
        DocumentReference docRef = firestore.collection("User").document(username);
        ApiFuture<DocumentSnapshot> future = docRef.get();
        DocumentSnapshot document = future.get();
        return document.exists();
    }

    public List<QueryDocumentSnapshot> checkRoomWith(String username)
            throws ExecutionException, InterruptedException {
        String me = SecurityContextHolder.getContext().getAuthentication().getName();

        Query query = firestore.collection("RoomChat")
                .whereArrayContains("member", me);

        ApiFuture<QuerySnapshot> querySnapshot = query.get();
        List<QueryDocumentSnapshot> documents = querySnapshot.get().getDocuments();

        List<QueryDocumentSnapshot> list = new ArrayList<>();
        for (QueryDocumentSnapshot document : documents) {
            List<String> members = (List<String>) document.get("member");
            assert members != null;
            if (members.contains(username)) {
                list.add(document);
            }
        }
        return list;
    }

    public void createChatUser(User user) {
        ChatUserResponse newUser = ChatUserResponse.builder()
                .avatar(user.getAvatar())
                .displayName(user.getFirstName() + " " + user.getLastName())
                .role(user.getRoles()
                        .stream().map(role -> UserRole.valueOf(role.getName()))
                        .toList())
                .build();
        firestore.collection("User").document(user.getUsername()).set(newUser);
    }

    public String createRoomChat(String username) throws ExecutionException, InterruptedException {
        String me = SecurityContextHolder.getContext().getAuthentication().getName();
        ChatRoomResponse newRoom = ChatRoomResponse.builder()
                .createdAt(Instant.now())
                .member(List.of(me, username))
                .build();
        return firestore.collection("RoomChat").add(newRoom).get().getId();
    }

    public void pushMessage(MessageRequest request)
            throws ExecutionException, InterruptedException {
        String me = SecurityContextHolder.getContext().getAuthentication().getName();

        if(!checkUser(request.getRecipient())) {
            createChatUser(userRepository.findByUsername(request.getRecipient())
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        }

        if(!checkUser(me)) {
            createChatUser(userRepository.findByUsername(me)
                    .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND)));
        }

        List<QueryDocumentSnapshot> rooms = checkRoomWith(request.getRecipient());
        String roomId;
        if(rooms.isEmpty()) {
            roomId = createRoomChat(request.getRecipient());
        }
        else {
            roomId = rooms.getFirst().getId();
        }



        MessageResponse newMessage = MessageResponse.builder()
                .type(request.getType())
                .createdBy(me)
                .createdAt(Timestamp.now())
                .content(request.getContent())
                .roomId(roomId)
                .build();

        DocumentReference messageRef = firestore.collection("Message").document();
        messageRef.set(newMessage).get();

        DocumentReference roomRef = firestore.collection("RoomChat").document(roomId);
        roomRef.update("lastMessage", messageRef).get();
    }
}
