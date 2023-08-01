package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<EmployeeEntity, Long> {
    // (select * from Employee where emp_id = ?)
    Optional<EmployeeEntity> findByEmpId(Long empId);
    //@Query("SELECT e FROM EmployeeEntity e WHERE e.emp_id = :empId")
    //Optional<EmployeeEntity> findByEmp_id(@Param("empId") Long empId);
}
