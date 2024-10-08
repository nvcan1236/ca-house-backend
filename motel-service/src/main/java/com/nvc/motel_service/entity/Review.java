package com.nvc.motel_service.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String userId;
    Instant createdAt;
    String createdBy;
    String content;

    @ManyToOne
    @JoinColumn(name = "motel_id", nullable = false)
    Motel motel;
}
