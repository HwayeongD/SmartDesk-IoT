package com.hjj.hjj_restful_server.service;


import com.hjj.hjj_restful_server.dto.EmployeeDTO;
import com.hjj.hjj_restful_server.entity.EmployeeEntity;
import com.hjj.hjj_restful_server.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeDTO login(Long empId, String password){
        Optional<EmployeeEntity> byEmpId = employeeRepository.findByEmpId(empId);
        if(byEmpId.isPresent()){
            EmployeeEntity employeeEntity = byEmpId.get();
            if(employeeEntity.getPassword().equals(password)){
                EmployeeDTO dto = EmployeeDTO.toEmployeeDTO(employeeEntity);
                return dto;
            }
            else{   // 비밀번호가 틀림
                EmployeeDTO dto = new EmployeeDTO();
                dto.setName("L202");
                return dto;
            }
        }
        else{   // 사용자가 존재하지 않음.
            EmployeeDTO dto = new EmployeeDTO();
            dto.setName("L201");
            return dto;
        }
    }

    public EmployeeDTO findByempId(Long empId){
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpId(empId);
        if(optionalEmployeeEntity.isPresent()){
            return EmployeeDTO.toEmployeeDTO(optionalEmployeeEntity.get());
        }
        else{
            return null;
        }
    }

    public EmployeeDTO findByEmpIdCard(String empIdCard){
        Optional<EmployeeEntity> optionalEmployeeEntity = employeeRepository.findByEmpIdCard(empIdCard);
        if(optionalEmployeeEntity.isPresent()){
            return EmployeeDTO.toEmployeeDTO(optionalEmployeeEntity.get());
        }
        else{
            return null;
        }
    }

    public void save(EmployeeDTO employeeDTO){
        EmployeeEntity employeeEntity = EmployeeEntity.toEmployeeEntity(employeeDTO);
        employeeRepository.save(employeeEntity);
    }



}
