package com.nvc.motel_service.entity;

import com.nvc.motel_service.enums.Job;
import com.nvc.motel_service.enums.PriceType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Requirement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    Double deposit;
    int contractAmount;
    boolean allowPet;
    String jobs;
    String other;

    @OneToOne
    @JoinColumn(name = "motel_id", nullable = false, unique = true)
    Motel motel;
}
