package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.DailyScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyScheduleRepository extends JpaRepository<DailyScheduleEntity, Long> {
    Optional<DailyScheduleEntity> findByEmpId(Long empId);

    @Query(value = "SELECT * FROM DailyScheduleEntity WHERE startTime <= NOW() AND endTime > NOW()", nativeQuery = true)
    List<DailyScheduleEntity> findNowSchedule();

    @Query(value = "SELECT * FROM DailyScheduleEntity WHERE DATE_FORMAT(endTime, '%Y-%m-%d %H:%i') = DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i')", nativeQuery = true)
    List<DailyScheduleEntity> findNowEndTime();

}
