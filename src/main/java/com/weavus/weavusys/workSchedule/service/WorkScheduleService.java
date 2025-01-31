package com.weavus.weavusys.workSchedule.service;

import com.weavus.weavusys.workSchedule.entity.WorkSchedule;
import com.weavus.weavusys.workSchedule.repo.WorkScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {
    private final WorkScheduleRepo workScheduleRepo; ;

    public List<WorkSchedule> getWorkSchedule(Long employeeId, int year, int month) {
        return workScheduleRepo.findByEmployeeAndWorkDateBetween(employeeId, year, month);
    }
}
