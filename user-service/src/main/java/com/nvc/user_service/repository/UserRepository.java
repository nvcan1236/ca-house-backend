package com.nvc.user_service.repository;

import com.nvc.user_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrId(String username, String id);

    @Query("SELECT count(u) from User u")
    long countAll();

    @Query("SELECT YEAR(u.createAt) as year, MONTH(u.createAt) as month, "
            + "SUM(CASE WHEN r.name = 'USER' THEN 1 ELSE 0 END) as countRole1, "
            + "SUM(CASE WHEN r.name = 'OWNER' THEN 1 ELSE 0 END) as countRole2 "
            + "FROM User u JOIN u.roles r "
            + "WHERE u.createAt BETWEEN :startDate AND :endDate "
            + "GROUP BY YEAR(u.createAt), MONTH(u.createAt) "
            + "ORDER BY year, month")
    List<Object[]> countUsersByMonth(@Param("startDate") LocalDate  startDate, @Param("endDate") LocalDate  endDate);

    @Query("SELECT QUARTER(u.createAt) as quarter, "
            + "SUM(CASE WHEN r.name = 'USER' THEN 1 ELSE 0 END) as countRole1, "
            + "SUM(CASE WHEN r.name = 'OWNER' THEN 1 ELSE 0 END) as countRole2 "
            + "FROM User u JOIN u.roles r "
            + "WHERE u.createAt BETWEEN :startDate AND :endDate "
            + "GROUP BY QUARTER(u.createAt) "
            + "ORDER BY quarter")
    List<Object[]> countUsersByQuarter(@Param("startDate") LocalDate  startDate, @Param("endDate") LocalDate  endDate);


    @Query("SELECT r.name, count(u.id) FROM User u JOIN u.roles r GROUP BY r.name")
    List<Object[]> countByRoles();
}
