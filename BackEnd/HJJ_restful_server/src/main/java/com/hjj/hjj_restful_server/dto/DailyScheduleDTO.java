package com.hjj.hjj_restful_server.dto;

import com.hjj.hjj_restful_server.entity.DailyScheduleEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DailyScheduleDTO {
    private Long dailyId;
    private Long empId;
    private java.sql.Timestamp startTime;
    private java.sql.Timestamp endTime;

    public static DailyScheduleDTO todailyScheduleDTO(DailyScheduleEntity dailyScheduleEntity){
        DailyScheduleDTO dailyScheduleDTO = new DailyScheduleDTO();
        dailyScheduleDTO.setEmpId(dailyScheduleEntity.getEmpId());
        dailyScheduleDTO.setDailyId(dailyScheduleEntity.getDailyId());
        dailyScheduleDTO.setStartTime(dailyScheduleEntity.getStartTime());
        dailyScheduleDTO.setEndTime(dailyScheduleEntity.getEndTime());
        return dailyScheduleDTO;
    }

}
