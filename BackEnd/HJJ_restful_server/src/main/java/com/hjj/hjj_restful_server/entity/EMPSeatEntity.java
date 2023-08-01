package com.hjj.hjj_restful_server.entity;

import com.hjj.hjj_restful_server.dto.EMPSeatDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "EMP_Seat")
public class EMPSeatEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.TABLE)
    private Long empId;

    @Column
    private Long prevSear;

    @Column
    private Long seatId;

    @Column
    private Long personalDeskHeight;

    @Column
    private boolean autoBook;

    public static EMPSeatEntity toEMPSeatEntity(EMPSeatDTO empSeatDTO) {
        EMPSeatEntity empSeatEntity = new EMPSeatEntity();
        empSeatEntity.setEmpId(empSeatDTO.getEmpId());
        empSeatEntity.setPrevSear(empSeatDTO.getPrevSear());
        empSeatEntity.setSeatId(empSeatDTO.getSeatId());
        empSeatEntity.setPersonalDeskHeight(empSeatDTO.getPersonalDeskHeight());
        empSeatEntity.setAutoBook(empSeatDTO.isAutoBook());
        return empSeatEntity;
    }
}
