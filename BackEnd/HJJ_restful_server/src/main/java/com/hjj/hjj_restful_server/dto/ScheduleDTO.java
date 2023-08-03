package com.hjj.hjj_restful_server.dto;

import com.hjj.hjj_restful_server.entity.ScheduleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class ScheduleDTO {
    private Long schId;
    private Long empId;
    private String head;
    private java.sql.Timestamp start;
    private java.sql.Timestamp end;
    private Byte status;
    private String detail;

    public static ScheduleDTO toScheduleDTO(ScheduleEntity scheduleEntity){
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        scheduleDTO.setSchId(scheduleEntity.getSchId());
        scheduleDTO.setEmpId(scheduleEntity.getEmpId());
        scheduleDTO.setHead(scheduleEntity.getHead());
        scheduleDTO.setStart(scheduleEntity.getStart());
        scheduleDTO.setEnd(scheduleEntity.getEnd());
        scheduleDTO.setStatus(scheduleEntity.getStatus());
        scheduleDTO.setDetail(scheduleEntity.getDetail());
        return scheduleDTO;
    }


}
