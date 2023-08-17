package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<ScheduleEntity, Long> {
    @Query(value = "SELECT * FROM (\n" +
            "    SELECT * FROM SERVER.Schedule \n" +
            "    WHERE empId = :empId AND start <= NOW() AND end > NOW()\n" +
            "    UNION\n" +
            "    SELECT * FROM SERVER.Schedule \n" +
            "    WHERE empId = :empId AND start > NOW()\n AND DATE(start) = CURDATE()" +
            "    ORDER BY start ASC\n" +
            ") AS combined\n" +
            "LIMIT 1", nativeQuery = true)
    Optional<ScheduleEntity> findRecentByEmpId(Long empId);

    @Query(value = "SELECT * FROM SERVER.Schedule WHERE empId = :empId AND YEAR(start) = :year AND MONTH(start) = :month ORDER BY start ASC", nativeQuery = true )
    List<ScheduleEntity> findByMonth(Long year, Long month, Long empId);

    @Query(value = "SELECT * FROM SERVER.Schedule WHERE empId = :empId AND DATE(start) = :date ORDER BY start ASC", nativeQuery = true)
    List<ScheduleEntity> findByDate(LocalDate date, Long empId);

    Optional<ScheduleEntity> findBySchId(Long schId);

    @Transactional
    Optional<ScheduleEntity> deleteBySchId(Long schId);

    List<ScheduleEntity> findByStatusAndStartBetween(Byte Status, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
