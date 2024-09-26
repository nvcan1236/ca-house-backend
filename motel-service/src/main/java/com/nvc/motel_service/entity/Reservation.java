package com.nvc.motel_service.entity;

import com.nvc.motel_service.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    Instant createdAt;
    ReservationStatus status;
    String createdBy;
    double amount;
    String content;


    @ManyToOne
    @JoinColumn(name = "motel_id", nullable = false)
    Motel motel;
}
