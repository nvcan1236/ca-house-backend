package com.nvc.motel_service.entity;

import com.nvc.motel_service.enums.MotelStatus;
import com.nvc.motel_service.enums.MotelType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
    String name;
    String description;
    Double price;
    MotelType type;
    Date availableDate;
    Double area;
    MotelStatus status;

    Date createdAt;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "motel")
    @JoinColumn(name = "location_id", referencedColumnName = "id")
    Location location;

    @OneToMany(mappedBy = "motel")
    Set<Amenity> amenities;

    @OneToMany(mappedBy = "motel")
    Set<MotelImage> images;

    @OneToMany(mappedBy = "motel")
    Set<Requirement> requirements;

    @OneToMany(mappedBy = "motel")
    Set<Price> prices;

    @OneToMany(mappedBy = "motel")
    Set<Review> reviews;
}
