package com.hjj.hjj_restful_server.service;

import com.hjj.hjj_restful_server.dto.DailyScheduleDTO;
import com.hjj.hjj_restful_server.dto.EMPAttendanceDTO;
import com.hjj.hjj_restful_server.entity.DailyScheduleEntity;
import com.hjj.hjj_restful_server.entity.EMPAttendanceEntity;
import com.hjj.hjj_restful_server.repository.DailyScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DailyScheduleService {

    private final DailyScheduleRepository dailyScheduleRepository;
    private final EMPAttendanceService empAttendanceService;

    public void DeleteBySchId(Long empId, java.sql.Timestamp start, java.sql.Timestamp end){
        List<DailyScheduleEntity> dailyScheduleEntity = dailyScheduleRepository.findByAllThing(empId, start, end);
        if(!dailyScheduleEntity.isEmpty()) {
            DailyScheduleEntity entityToDelete = dailyScheduleEntity.get(0);
            dailyScheduleRepository.deleteByDailyId(entityToDelete.getDailyId());
            EMPAttendanceDTO empAttendanceDTO = empAttendanceService.findByempId(empId);
            empAttendanceDTO.setStatus(Byte.valueOf("1"));
            empAttendanceService.save(empAttendanceDTO);
        }
    }

    public List<DailyScheduleDTO> findNowSchedule(){
        List<DailyScheduleEntity> optionalDailyScheduleEntity = dailyScheduleRepository.findNowSchedule();
        if(optionalDailyScheduleEntity.size() == 0){    // Empty
            return null;
        }
        else{
            List<DailyScheduleDTO> dailyScheduleDTOList = new ArrayList<>();
            for (DailyScheduleEntity entity : optionalDailyScheduleEntity) {
                DailyScheduleDTO dto = DailyScheduleDTO.todailyScheduleDTO(entity);
                dailyScheduleDTOList.add(dto);
            }
            return dailyScheduleDTOList;
        }
    }

    public List<DailyScheduleDTO> findNowEndTime(){
        List<DailyScheduleEntity> optionalDailyScheduleEntity = dailyScheduleRepository.findNowEndTime();
        if(optionalDailyScheduleEntity.size() == 0){    // Empty
            return null;
        }
        else{
            List<DailyScheduleDTO> dailyScheduleDTOList = new ArrayList<>();
            for (DailyScheduleEntity entity : optionalDailyScheduleEntity) {
                DailyScheduleDTO dto = DailyScheduleDTO.todailyScheduleDTO(entity);
                dailyScheduleDTOList.add(dto);
            }
            return dailyScheduleDTOList;
        }
    }

    public void save(DailyScheduleDTO dailyScheduleDTO){
        DailyScheduleEntity dailyScheduleEntity = DailyScheduleEntity.toDailyScheduleEntity(dailyScheduleDTO);
        dailyScheduleRepository.save(dailyScheduleEntity);
    }
}
