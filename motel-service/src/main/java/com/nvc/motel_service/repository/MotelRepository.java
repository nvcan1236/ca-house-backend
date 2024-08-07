package com.nvc.motel_service.repository;

import com.nvc.motel_service.entity.Motel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MotelRepository extends JpaRepository<Motel, String> {
}
