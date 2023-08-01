package com.hjj.hjj_restful_server.entity;

import com.hjj.hjj_restful_server.dto.DeskDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@Table(name = "Desk")
public class DeskEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long seatId;

    @Column
    private Long empId;

    @Column
    private Long deskHeightNow;

    @Column
    private String seatIp;

    @Column
    private Long floor;

    public static DeskEntity toDeskEntity(DeskDTO deskDTO){
        DeskEntity deskEntity = new DeskEntity();
        deskEntity.setSeatId(deskDTO.getSeatId());
        deskEntity.setDeskHeightNow(deskDTO.getDeskHeightNow());
        deskEntity.setEmpId(deskDTO.getEmpId());
        deskEntity.setSeatIp(deskDTO.getSeatIp());
        deskEntity.setFloor(deskDTO.getFloor());
        return deskEntity;
    }
}
