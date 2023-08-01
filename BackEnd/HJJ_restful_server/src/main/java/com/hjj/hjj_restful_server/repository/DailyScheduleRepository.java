package com.hjj.hjj_restful_server.repository;

import com.hjj.hjj_restful_server.entity.DailyScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyScheduleRepository extends JpaRepository<DailyScheduleEntity, Long> {
}
