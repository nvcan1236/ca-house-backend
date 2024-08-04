package com.nvc.ca_house.repository;

import com.nvc.ca_house.entity.InvalidatedToken;
import com.nvc.ca_house.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
