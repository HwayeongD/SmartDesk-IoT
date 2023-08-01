package com.hjj.hjj_restful_server.dto;

import com.hjj.hjj_restful_server.entity.DepartmentEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DepartmentDTO {
    private Long teamId;
    private String teamName;

    public static DepartmentDTO toDepartmentDTO(DepartmentEntity departmentEntity) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setTeamId(departmentEntity.getTeamId());
        departmentDTO.setTeamName(departmentEntity.getTeamName());
        return departmentDTO;
    }
}
