package com.hjj.hjj_restful_server.dto;

import com.hjj.hjj_restful_server.entity.EMPAttendanceEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EMPAttendanceDTO {
    private Long empId;
    private Time workAttTime;
    private Time workEndTime;
    private Byte status;

    public static EMPAttendanceDTO toEMPAttendanceDTO(EMPAttendanceEntity empAttendanceEntity){
        EMPAttendanceDTO empAttendanceDTO = new EMPAttendanceDTO();
        empAttendanceDTO.setEmpId(empAttendanceEntity.getEmpId());
        empAttendanceDTO.setWorkAttTime(empAttendanceEntity.getWorkAttTime());
        empAttendanceDTO.setWorkEndTime(empAttendanceEntity.getWorkEndTime());
        empAttendanceDTO.setStatus(empAttendanceEntity.getStatus());
        return empAttendanceDTO;
    }
}
