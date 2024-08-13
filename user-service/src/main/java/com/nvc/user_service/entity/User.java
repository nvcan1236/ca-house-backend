package com.nvc.user_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String username;
    String password;
    String firstName;
    String lastName;
    String email;
    String avatar;
    boolean isActive = true;

    @OneToOne
    Profile profile;

    @ManyToMany
    Set<Role> roles;

//    @OneToMany(mappedBy = "follower")
//    Set<Friendship> follower;
//
//    @OneToMany(mappedBy = "following")
//    Set<Friendship> following;

    @ManyToMany
    @JoinTable(
            name = "friendship", // Tên của bảng trung gian
            joinColumns = @JoinColumn(name = "follower"), // Tên cột tham chiếu đến bảng hiện tại
            inverseJoinColumns = @JoinColumn(name = "following") // Tên cột tham chiếu đến bảng kia
    )
    private Set<User> following = new HashSet<>();

    @ManyToMany(mappedBy = "following")
    private Set<User> follower = new HashSet<>();
}
