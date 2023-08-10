package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.EMPSeatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface EMPSeatRepository extends JpaRepository<EMPSeatEntity, Long> {
    Optional<EMPSeatEntity> findByEmpId(Long empId);

    @Modifying
    @Transactional
    @Query(value="UPDATE SERVER.EMP_Seat SET seatId = NULL" , nativeQuery = true)
    void resetTable();
}
