package com.nvc.motel_service.entity;

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
public class MotelImage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String url;

    @ManyToOne
    @JoinColumn(name = "motel_id", nullable = false)
    Motel motel;
}
