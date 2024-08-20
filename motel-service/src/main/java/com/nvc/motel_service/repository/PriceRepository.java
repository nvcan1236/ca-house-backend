package com.nvc.motel_service.repository;

import com.nvc.motel_service.entity.Amenity;
import com.nvc.motel_service.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface PriceRepository extends JpaRepository<Price, String> {
}
