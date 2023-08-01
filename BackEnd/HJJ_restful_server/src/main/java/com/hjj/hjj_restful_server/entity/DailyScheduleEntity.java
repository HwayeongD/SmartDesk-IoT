package com.hjj.hjj_restful_server.entity;

import com.hjj.hjj_restful_server.dto.DailyScheduleDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Daily_Schedule")
public class DailyScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long dailyId;

    @Column
    private Long empId;

    @Column
    private java.sql.Timestamp startTime;

    @Column
    private java.sql.Timestamp endTime;

    public static DailyScheduleEntity toDailyScheduleEntity(DailyScheduleDTO dailyScheduleDTO) {
        DailyScheduleEntity dailyScheduleEntity = new DailyScheduleEntity();
        dailyScheduleEntity.setDailyId(dailyScheduleDTO.getDailyId());
        dailyScheduleEntity.setEmpId(dailyScheduleDTO.getEmpId());
        dailyScheduleEntity.setStartTime(dailyScheduleDTO.getStartTime());
        dailyScheduleEntity.setEndTime(dailyScheduleDTO.getEndTime());
        return dailyScheduleEntity;
    }
}
