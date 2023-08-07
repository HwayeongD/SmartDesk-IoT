package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.DeskEntity;
import com.hjj.hjj_restful_server.entity.EMPAttendanceEntity;
import com.hjj.hjj_restful_server.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface EMPAttendanceRepository extends JpaRepository<EMPAttendanceEntity, Long> {
    Optional<EMPAttendanceEntity> findByEmpId(Long empId);

//    @Query(value="SELECT * FROM SERVER.Desk WHERE floor = :floor ORDER BY seatId ASC", nativeQuery = true)
//    List<DeskEntity> findByFloor(Long floor);

    @Modifying
    @Transactional
    @Query(value="UPDATE SERVER.EMP_Attendance SET workAttTime = NULL, workEndTime = NULL, status = 0" , nativeQuery = true)
    void resetTable();
}
