package com.nvc.motel_service.repository;

import com.nvc.motel_service.entity.Amenity;
import com.nvc.motel_service.entity.Bill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface BillRepository extends JpaRepository<Bill, String> {
}
