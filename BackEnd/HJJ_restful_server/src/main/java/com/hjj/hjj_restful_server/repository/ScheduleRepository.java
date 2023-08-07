package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    @Query(value = "SELECT * FROM SERVER.Schedule WHERE empId = :empId AND start > NOW() ORDER BY start ASC LIMIT 1", nativeQuery = true)
    Optional<ScheduleEntity> findRecentByEmpId(Long empId);

    @Query(value = "SELECT * FROM SERVER.Schedule WHERE MONTH(start) = :month ORDER BY start ASC", nativeQuery = true )
    List<ScheduleEntity> findByMonth(Long month);

    Optional<ScheduleEntity> findBySchId(Long schId);

    @Transactional
    Optional<ScheduleEntity> deleteBySchId(Long schId);
}
