package com.hjj.hjj_restful_server.service;

import com.hjj.hjj_restful_server.dto.ScheduleDTO;
import com.hjj.hjj_restful_server.entity.DailyScheduleEntity;
import com.hjj.hjj_restful_server.entity.ScheduleEntity;
import com.hjj.hjj_restful_server.repository.DailyScheduleRepository;
import com.hjj.hjj_restful_server.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ScheduleService {

    private final ScheduleRepository scheduleRepository;
    private final DailyScheduleRepository dailyScheduleRepository;

    public ScheduleDTO findBySchId(Long schId){
        Optional<ScheduleEntity> optionalScheduleEntity = scheduleRepository.findBySchId(schId);
        if(optionalScheduleEntity.isPresent()){
            return ScheduleDTO.toScheduleDTO(optionalScheduleEntity.get());
        }
        else{
            return null;
        }
    }

    public ScheduleDTO findRecentByEmpId(Long empId){
        Optional<ScheduleEntity> optionalScheduleEntity = scheduleRepository.findRecentByEmpId(empId);
        if(optionalScheduleEntity.isPresent()){
            return ScheduleDTO.toScheduleDTO(optionalScheduleEntity.get());
        }
        else{
            return null;
        }
    }

    public List<ScheduleDTO> findByMonth(Long year, Long month, Long empId){
        List<ScheduleEntity> opthScheduleEntityList = scheduleRepository.findByMonth(year, month, empId);
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

    public List<ScheduleDTO> findByDate(LocalDate date, Long empId){
        List<ScheduleEntity> scheduleEntityList = scheduleRepository.findByDate(date, empId);
        if(scheduleEntityList.size() == 0){
            return null;
        }
        else{
            List<ScheduleDTO> scheduleDTOList = new ArrayList<>();
            for(ScheduleEntity entity : scheduleEntityList){
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

    public void deleteSchedule(Long schId){
        scheduleRepository.deleteBySchId(schId);
    }

    @Transactional
    public void transferToDailySchedule(){
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfDay = now.toLocalDate().atStartOfDay();
        LocalDateTime endOfDay = now.toLocalDate().atTime(23,59,59);
        List<ScheduleEntity> scheduleEntities = scheduleRepository.findByStatusAndStartBetween(Byte.valueOf("2"),startOfDay,endOfDay);
        for(ScheduleEntity scheduleEntity : scheduleEntities){
            DailyScheduleEntity dailyScheduleEntity = new DailyScheduleEntity();
            dailyScheduleEntity.setEmpId(scheduleEntity.getEmpId());
            dailyScheduleEntity.setStartTime(scheduleEntity.getStart());
            dailyScheduleEntity.setEndTime(scheduleEntity.getEnd());
            dailyScheduleRepository.save(dailyScheduleEntity);
        }
    }

}
