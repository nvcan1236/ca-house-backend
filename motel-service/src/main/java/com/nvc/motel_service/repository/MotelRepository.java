package com.nvc.motel_service.repository;

import com.nvc.motel_service.entity.Motel;
import com.nvc.motel_service.enums.MotelType;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;


@Repository
public interface MotelRepository extends JpaRepository<Motel, String> {
    @Query("SELECT m FROM Motel m LEFT JOIN m.amenities a "
            + "WHERE (:roomType IS NULL OR m.type = :roomType) "
            + "AND (:isAdmin = TRUE OR (m.isApproved = TRUE)) "
            + "AND (:minPrice IS NULL OR m.price >= :minPrice) "
            + "AND (:maxPrice IS NULL OR m.price <= :maxPrice) "
            + "AND (:amenities IS NULL OR :size = 0 OR a.name IN :amenities) "
            + "GROUP BY m.id "
            + "HAVING (:size = 0 OR COUNT(a.id) = :size)")
    Page<Motel> findAllFiltered(Pageable pageable,
                                @Param("isAdmin") boolean isAdmin,
                                @Param("roomType") MotelType roomType,
                                @Param("minPrice") Double minPrice,
                                @Param("maxPrice") Double maxPrice,
                                @Param("amenities") List<String> amenities,
                                @Param("size") long size);

    @Query("SELECT c FROM Motel c WHERE function('ST_DWithin', c.location.point, :point, :distance) = true")
    List<Motel> findNearestMotels(Point point, double distance);

    List<Motel> findByOwnerId(String ownerId);

    @Query("SELECT ROUND(m.area / 5) as areaGroup, COUNT(m) as countMotel "
     + "FROM Motel m "
     + "GROUP BY areaGroup "
     + "ORDER BY areaGroup")
    List<Object[]> statByPrices();

    @Query("SELECT ROUND(m.price / 2000000) as priceGroup, COUNT(m) as countMotel "
            + "FROM Motel m "
            + "GROUP BY priceGroup "
            + "ORDER BY priceGroup")
    List<Object[]> statByArea();
    @Query("SELECT YEAR(m.createdAt) as year, MONTH(m.createdAt) as month, count (m.id)"
            + "FROM Motel m "
            + "WHERE m.createdAt BETWEEN :startDate AND :endDate "
            + "GROUP BY YEAR(m.createdAt), MONTH(m.createdAt) "
            + "ORDER BY year, month")
    List<Object[]> statByTime(@Param("startDate") Instant startDate, @Param("endDate") Instant  endDate);

    @Query("SELECT m.type, count(m.id) FROM Motel m GROUP BY m.type ORDER BY m.type")
    List<Object[]> statByType();
}
