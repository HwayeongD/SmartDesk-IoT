package com.hjj.hjj_restful_server.service;

import com.hjj.hjj_restful_server.dto.EMPSeatDTO;
import com.hjj.hjj_restful_server.entity.EMPSeatEntity;
import com.hjj.hjj_restful_server.repository.EMPSeatRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EMPSeatService {
    private final EMPSeatRepository empSeatRepository;

    public EMPSeatDTO findByempId(Long empId){
        Optional<EMPSeatEntity> optionalEMPSeatEntity = empSeatRepository.findByEmpId(empId);
        if(optionalEMPSeatEntity.isPresent()){
            return EMPSeatDTO.toEMPSeatDTO(optionalEMPSeatEntity.get());
        }
        else{
            return null;
        }
    }

    public void save(EMPSeatDTO empSeatDTO){
        EMPSeatEntity empSeatEntity = EMPSeatEntity.toEMPSeatEntity(empSeatDTO);
        empSeatRepository.save(empSeatEntity);
    }


}
