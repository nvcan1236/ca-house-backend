package com.nvc.motel_service.repository;

import com.nvc.motel_service.entity.MotelImage;
import com.nvc.motel_service.entity.Price;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MotelImageRepository extends JpaRepository<MotelImage, String> {
}
