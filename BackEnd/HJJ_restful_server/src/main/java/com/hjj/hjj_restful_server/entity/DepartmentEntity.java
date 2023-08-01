package com.hjj.hjj_restful_server.entity;

import com.hjj.hjj_restful_server.dto.DepartmentDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "Department")
public class DepartmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long teamId;

    @Column
    private String teamName;

    public static DepartmentEntity toDepartmentEntity(DepartmentDTO departmentDTO) {
        DepartmentEntity departmentEntity = new DepartmentEntity();
        departmentEntity.setTeamId(departmentDTO.getTeamId());
        departmentEntity.setTeamName(departmentDTO.getTeamName());
        return departmentEntity;
    }
}
