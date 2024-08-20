package com.nvc.motel_service.entity;

import com.nvc.motel_service.enums.MotelStatus;
import com.nvc.motel_service.enums.MotelType;
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
public class Amenity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String name;
    String description;

    @ManyToOne
    @JoinColumn(name = "motel_id", nullable = false)
    Motel motel;
}
