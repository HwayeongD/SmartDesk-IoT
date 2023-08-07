package com.hjj.hjj_restful_server.service;

import com.hjj.hjj_restful_server.dto.EMPAttendanceDTO;
import com.hjj.hjj_restful_server.entity.EMPAttendanceEntity;
import com.hjj.hjj_restful_server.repository.EMPAttendanceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EMPAttendanceService {

    private final EMPAttendanceRepository empAttendanceRepository;

    public EMPAttendanceDTO findByempId(Long empId){
        Optional<EMPAttendanceEntity> optionalEMPAttendanceEntity = empAttendanceRepository.findByEmpId(empId);
        if(optionalEMPAttendanceEntity.isPresent()){
            return EMPAttendanceDTO.toEMPAttendanceDTO(optionalEMPAttendanceEntity.get());
        }
        else{
            return null;
        }
    }

    public void save(EMPAttendanceDTO empAttendanceDTO){
        EMPAttendanceEntity empAttendanceEntity = EMPAttendanceEntity.toEMPAttendanceEntity(empAttendanceDTO);
        empAttendanceRepository.save(empAttendanceEntity);
    }

}
