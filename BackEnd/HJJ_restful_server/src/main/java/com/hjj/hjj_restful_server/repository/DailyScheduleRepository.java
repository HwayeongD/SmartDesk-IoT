package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.DailyScheduleEntity;
import com.hjj.hjj_restful_server.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface DailyScheduleRepository extends JpaRepository<DailyScheduleEntity, Long> {
    Optional<DailyScheduleEntity> findByEmpId(Long empId);

    @Query(value = "SELECT * FROM SERVER.Daily_Schedule WHERE startTime <= NOW() AND endTime > NOW()", nativeQuery = true)
    List<DailyScheduleEntity> findNowSchedule();

    @Query(value = "SELECT * FROM SERVER.Daily_Schedule WHERE DATE_FORMAT(endTime, '%Y-%m-%d %H:%i') = DATE_FORMAT(NOW(), '%Y-%m-%d %H:%i')", nativeQuery = true)
    List<DailyScheduleEntity> findNowEndTime();

    @Query(value = "SELECT * FROM SERVER.Daily_Schedule WHERE empId = :empId AND startTime = :start AND endTime = :end", nativeQuery = true)
    List<DailyScheduleEntity> findByAllThing(Long empId,java.sql.Timestamp start, java.sql.Timestamp end);

    @Transactional
    Optional<DailyScheduleEntity> deleteByDailyId(Long dailyId);

    @Modifying
    @Transactional
    @Query(value = "TRUNCATE TABLE SERVER.Daily_Schedule", nativeQuery = true)
    void truncateTable();
}
