package com.hjj.hjj_restful_server.service;

import com.hjj.hjj_restful_server.dto.DailyScheduleDTO;
import com.hjj.hjj_restful_server.entity.DailyScheduleEntity;
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

    public DailyScheduleDTO findNowEndTime(){
        Optional<DailyScheduleEntity> optionalDailyScheduleDTO = dailyScheduleRepository.findNowEndTime();
        if(optionalDailyScheduleDTO.isPresent()){
            return DailyScheduleDTO.todailyScheduleDTO(optionalDailyScheduleDTO.get());
        }
        else{
            return null;
        }
    }

    public void save(DailyScheduleDTO dailyScheduleDTO){
        DailyScheduleEntity dailyScheduleEntity = DailyScheduleEntity.toDailyScheduleEntity(dailyScheduleDTO);
        dailyScheduleRepository.save(dailyScheduleEntity);
    }
}
