package com.nvc.motel_service.entity;

import com.nvc.motel_service.enums.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Bill {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

}
