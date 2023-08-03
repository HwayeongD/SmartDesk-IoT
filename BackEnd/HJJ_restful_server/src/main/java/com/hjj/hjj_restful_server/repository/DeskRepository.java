package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.DeskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeskRepository extends JpaRepository<DeskEntity, Long> {
    Optional<DeskEntity> findBySeatId(Long seatId);

    Optional<DeskEntity> findByEmpId(Long empId);

    @Query(value="SELECT * FROM SERVER.Desk WHERE floor = :floor ORDER BY seatId ASC", nativeQuery = true)
    List<DeskEntity> findByFloor(Long floor);
}
