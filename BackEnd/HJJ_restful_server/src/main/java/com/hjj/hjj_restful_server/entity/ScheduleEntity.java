package com.hjj.hjj_restful_server.entity;

import com.hjj.hjj_restful_server.dto.ScheduleDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Schedule")
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long schId;

    @Column
    private Long empId;

    @Column
    private java.sql.Timestamp start;

    @Column
    private java.sql.Timestamp end;

    @Column
    private String status;

    @Column
    private String detail;

    public static ScheduleEntity toScheduleEntity(ScheduleDTO scheduleDTO){
        ScheduleEntity scheduleEntity = new ScheduleEntity();
        scheduleEntity.setSchId(scheduleDTO.getSchId());
        scheduleEntity.setEmpId(scheduleDTO.getEmpId());
        scheduleEntity.setStart(scheduleDTO.getStart());
        scheduleEntity.setEnd(scheduleDTO.getEnd());
        scheduleEntity.setStatus(scheduleDTO.getStatus());
        scheduleEntity.setDetail(scheduleDTO.getDetail());
        return scheduleEntity;
    }
}
