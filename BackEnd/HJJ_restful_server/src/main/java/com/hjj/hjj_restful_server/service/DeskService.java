package com.hjj.hjj_restful_server.service;

import com.hjj.hjj_restful_server.dto.DeskDTO;
import com.hjj.hjj_restful_server.entity.DeskEntity;
import com.hjj.hjj_restful_server.repository.DeskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeskService {
    private final DeskRepository deskRepository;

    public DeskDTO findByseatId(Long seatId){
        Optional<DeskEntity> optionalDeskEntity = deskRepository.findBySeatId(seatId);
        if(optionalDeskEntity.isPresent()){
            return DeskDTO.toDeskDTO(optionalDeskEntity.get());
        }
        else{
            return null;
        }
    }
}
