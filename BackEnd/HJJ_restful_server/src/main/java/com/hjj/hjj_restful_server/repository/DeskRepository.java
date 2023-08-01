package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.DeskEntity;
import com.hjj.hjj_restful_server.entity.EMPSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeskRepository extends JpaRepository<DeskEntity, Long> {
    Optional<DeskEntity> findBySeatId(Long seatId);
}
