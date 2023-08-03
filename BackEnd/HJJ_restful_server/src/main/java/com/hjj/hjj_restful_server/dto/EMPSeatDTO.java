package com.hjj.hjj_restful_server.dto;

import com.hjj.hjj_restful_server.entity.EMPSeatEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class EMPSeatDTO {
    private Long empId;
    private Long prevSeat;
    private Long seatId;
    private Long personalDeskHeight;
    private boolean autoBook;

    public static EMPSeatDTO toEMPSeatDTO(EMPSeatEntity empSeatEntity) {
        EMPSeatDTO empSeatDTO = new EMPSeatDTO();
        empSeatDTO.setEmpId(empSeatEntity.getEmpId());
        empSeatDTO.setPrevSeat(empSeatEntity.getPrevSeat());
        empSeatDTO.setSeatId(empSeatEntity.getSeatId());
        empSeatDTO.setPersonalDeskHeight(empSeatEntity.getPersonalDeskHeight());
        empSeatDTO.setAutoBook(empSeatEntity.isAutoBook());

        return empSeatDTO;
    }
}
