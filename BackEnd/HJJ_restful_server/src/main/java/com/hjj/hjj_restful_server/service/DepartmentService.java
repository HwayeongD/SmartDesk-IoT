package com.hjj.hjj_restful_server.service;

import com.hjj.hjj_restful_server.dto.DepartmentDTO;
import com.hjj.hjj_restful_server.entity.DepartmentEntity;
import com.hjj.hjj_restful_server.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DepartmentService {
    private final DepartmentRepository departmentRepository;

    public DepartmentDTO findByTeamId(Long teamId){
        Optional<DepartmentEntity> optionalDepartmentEntity = departmentRepository.findByTeamId(teamId);
        if(optionalDepartmentEntity.isPresent()){
            return DepartmentDTO.toDepartmentDTO(optionalDepartmentEntity.get());
        }
        else{
            return null;
        }
    }

    public void save(DepartmentDTO departmentDTO){
        DepartmentEntity departmentEntity = DepartmentEntity.toDepartmentEntity(departmentDTO);
        departmentRepository.save(departmentEntity);
    }
}
