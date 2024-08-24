package com.nvc.motel_service.repository;

import com.nvc.motel_service.entity.Motel;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface MotelRepository extends JpaRepository<Motel, String> {
    Page<Motel> findAll(Pageable pageable);

    @Query("SELECT c FROM Motel c WHERE function('ST_DWithin', c.location.point, :point, :distance) = true")
    List<Motel> findNearestMotels(Point point, double distance);
}
