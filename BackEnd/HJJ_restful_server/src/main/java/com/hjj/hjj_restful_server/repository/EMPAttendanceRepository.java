package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.EMPAttendanceEntity;
import com.hjj.hjj_restful_server.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EMPAttendanceRepository extends JpaRepository<EMPAttendanceEntity, Long> {
    Optional<EMPAttendanceEntity> findByEmpId(Long empId);
}
