package com.nvc.motel_service.entity;

import com.nvc.motel_service.enums.PriceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Price {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    PriceType type;
    String description;
    Double value;
    String unit;

    @ManyToOne
    @JoinColumn(name = "motel_id", nullable = false)
    Motel motel;
}
