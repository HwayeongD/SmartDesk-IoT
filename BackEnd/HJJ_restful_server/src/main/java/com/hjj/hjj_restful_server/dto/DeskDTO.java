package com.hjj.hjj_restful_server.dto;

import com.hjj.hjj_restful_server.entity.DeskEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DeskDTO {
    private Long seatId;
    private Long empId;
    private Long deskHeightNow;
    private String seatIp;
    private Long floor;

    public static DeskDTO toDeskDTO(DeskEntity deskEntity){
        DeskDTO deskDTO = new DeskDTO();
        deskDTO.setSeatId(deskEntity.getSeatId());
        deskDTO.setEmpId(deskEntity.getEmpId());
        deskDTO.setDeskHeightNow(deskEntity.getDeskHeightNow());
        deskDTO.setSeatIp(deskEntity.getSeatIp());
        deskDTO.setFloor(deskEntity.getFloor());
        return deskDTO;
    }
}
