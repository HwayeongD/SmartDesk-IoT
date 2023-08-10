package com.hjj.hjj_restful_server.entity;

import com.hjj.hjj_restful_server.dto.EmployeeDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "Employee")
public class EmployeeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long empId;

    @Column
    private String name;

    @Column
    private String nickname;

    @Column
    private String password;

    @Column
    private Long teamId;

    @Column
    private String empIdCard;


    public static EmployeeEntity toEmployeeEntity(EmployeeDTO employeeDTO){
        EmployeeEntity employeeEntity = new EmployeeEntity();
        employeeEntity.setEmpId(employeeDTO.getEmpId());
        employeeEntity.setName(employeeDTO.getName());
        employeeEntity.setNickname(employeeDTO.getNickname());
        employeeEntity.setPassword(employeeDTO.getPassword());
        employeeEntity.setTeamId(employeeDTO.getTeamId());
        employeeEntity.setEmpIdCard(employeeDTO.getEmpIdCard());
        return employeeEntity;
    }
}
