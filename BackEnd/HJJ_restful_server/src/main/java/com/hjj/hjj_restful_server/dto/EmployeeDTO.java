package com.hjj.hjj_restful_server.dto;

import com.hjj.hjj_restful_server.entity.EmployeeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EmployeeDTO {
    private Long empId;
    private String name;
    private String nickname;
    private String password;
    private Long teamId;
    private String empIdCard;

    public static EmployeeDTO toEmployeeDTO(EmployeeEntity employeeEntity)
    {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmpId(employeeEntity.getEmpId());
        employeeDTO.setName(employeeEntity.getName());
        employeeDTO.setNickname(employeeEntity.getNickname());
        employeeDTO.setPassword(employeeEntity.getPassword());
        employeeDTO.setTeamId(employeeEntity.getTeamId());
        employeeDTO.setEmpIdCard(employeeEntity.getEmpIdCard());
        return employeeDTO;
    }
}
