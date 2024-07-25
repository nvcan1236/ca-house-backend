package com.nvc.ca_house.service;

import com.nvc.ca_house.dto.request.UserCreationRequest;
import com.nvc.ca_house.entity.User;
import com.nvc.ca_house.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public User createUser(UserCreationRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());

        return userRepository.save(user);
    }

    public List<User> getUserList() {
        return userRepository.findAll();
    }

    public User getUserById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void deleteUser(String id) {
        User user = userRepository.findById(id).orElse(null);
        if(user==null) {
            return;
        }
        user.setActive(false);
        userRepository.save(user);
    }
 }
