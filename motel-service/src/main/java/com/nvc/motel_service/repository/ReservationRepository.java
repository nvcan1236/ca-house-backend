package com.nvc.motel_service.repository;

import com.nvc.motel_service.entity.Reservation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface ReservationRepository extends JpaRepository<Reservation, String> {
    Page<Reservation> findAllByCreatedBy(Pageable pageable, String userId);
}
