package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    @Query(value = "SELECT * FROM Schedule WHERE empId = :empId AND start > NOW() ORDER BY start ASC LIMIT 1", nativeQuery = true)
    Optional<ScheduleEntity> findRecentByEmpId(Long empId);
}
