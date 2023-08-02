package com.hjj.hjj_restful_server.entity;

import com.hjj.hjj_restful_server.dto.EMPAttendanceDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Time;

@Entity
@Setter
@Getter
@Table(name = "EMP_Attendance")
public class EMPAttendanceEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long empId;

    @Column(name = "workAttTime")
    private Time workAttTime;

    @Column(name = "workEndTime")
    private Time workEndTime;

    @Column(name = "status")
    private byte status;

    // Constructors, getters, and setters can be added as needed.

    public static EMPAttendanceEntity toEMPAttendanceEntity(EMPAttendanceDTO empAttendanceDTO) {
        EMPAttendanceEntity empAttendanceEntity = new EMPAttendanceEntity();
        empAttendanceEntity.setEmpId(empAttendanceDTO.getEmpId());
        empAttendanceEntity.setWorkAttTime(empAttendanceDTO.getWorkAttTime());
        empAttendanceEntity.setWorkEndTime(empAttendanceDTO.getWorkEndTime());
        empAttendanceEntity.setStatus(empAttendanceDTO.getStatus());
        return empAttendanceEntity;
    }
}
