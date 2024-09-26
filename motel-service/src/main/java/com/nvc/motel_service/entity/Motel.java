package com.nvc.motel_service.entity;

import com.nvc.motel_service.enums.MotelStatus;
import com.nvc.motel_service.enums.MotelType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.Instant;
import java.util.Date;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Motel {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String ownerId;
    String name;
    String description;
    Double price;
    MotelType type;
    Instant availableDate;
    Double area;
    MotelStatus status;
    boolean isApprove=false;

    Instant createdAt;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "motel")
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    Location location;

    @OneToMany(mappedBy = "motel")
    Set<Amenity> amenities;

    @OneToMany(mappedBy = "motel")
    Set<MotelImage> images;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "motel")
    Requirement requirement;

    @OneToMany(mappedBy = "motel")
    Set<Price> prices;

    @OneToMany(mappedBy = "motel")
    Set<Review> reviews;
}
