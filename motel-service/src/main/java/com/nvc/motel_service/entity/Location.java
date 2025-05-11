package com.nvc.motel_service.entity;

import com.nvc.motel_service.enums.MotelStatus;
import com.nvc.motel_service.enums.MotelType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.locationtech.jts.geom.Point;

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
    @Column(columnDefinition = "geography(Point, 4326)")
    private Point point;
    @OneToOne()
    Motel motel;

    public String getFullLocation() {
        StringBuilder fullAddress = new StringBuilder();

        if (other != null && !other.isBlank()) fullAddress.append(other).append(", ");
        if (street != null && !street.isBlank()) fullAddress.append(street).append(", ");
        if (ward != null && !ward.isBlank()) fullAddress.append("Phường ").append(ward).append(", ");
        if (district != null && !district.isBlank()) fullAddress.append("Quận ").append(district).append(", ");
        if (city != null && !city.isBlank()) fullAddress.append(city);

        String result = fullAddress.toString().trim();
        if (result.endsWith(",")) {
            result = result.substring(0, result.length() - 1);
        }

        return result;
    }
}
