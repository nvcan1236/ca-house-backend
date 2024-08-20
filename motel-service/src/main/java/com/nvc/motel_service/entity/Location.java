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
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String city;
    String district;
    String ward;
    String street;
    String other;
    Double longitude;
    Double latitude;
    @OneToOne()
    Motel motel;
}
