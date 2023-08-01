package com.hjj.hjj_restful_server.service;

import com.hjj.hjj_restful_server.dto.ScheduleDTO;
import com.hjj.hjj_restful_server.entity.ScheduleEntity;
import com.hjj.hjj_restful_server.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

}
