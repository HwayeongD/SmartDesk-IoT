package com.hjj.hjj_restful_server.service;

import com.hjj.hjj_restful_server.dto.ScheduleDTO;
import com.hjj.hjj_restful_server.entity.ScheduleEntity;
import com.hjj.hjj_restful_server.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;

    public ScheduleDTO findRecentByEmpId(Long empId){
        Optional<ScheduleEntity> optionalScheduleEntity = scheduleRepository.findRecentByEmpId(empId);
        if(optionalScheduleEntity.isPresent()){
            return ScheduleDTO.toScheduleDTO(optionalScheduleEntity.get());
        }
        else{
            return null;
        }
    }

    public List<ScheduleDTO> findByMonth(Long month){
        List<ScheduleEntity> opthScheduleEntityList = scheduleRepository.findByMonth(month);
        if(opthScheduleEntityList.size()==0){
            return null;
        }
        else{
            List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
            for(ScheduleEntity entity : opthScheduleEntityList){
                ScheduleDTO dto = ScheduleDTO.toScheduleDTO(entity);
                scheduleDTOList.add(dto);
            }
            return scheduleDTOList;
        }
    }

    public void save(ScheduleDTO scheduleDTO){
        ScheduleEntity scheduleEntity = ScheduleEntity.toScheduleEntity(scheduleDTO);
        scheduleRepository.save(scheduleEntity);
    }

}
