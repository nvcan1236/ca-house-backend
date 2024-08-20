package com.nvc.motel_service.repository;

import com.nvc.motel_service.entity.Amenity;
import com.nvc.motel_service.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ReviewRepository extends JpaRepository<Review, String> {
}
