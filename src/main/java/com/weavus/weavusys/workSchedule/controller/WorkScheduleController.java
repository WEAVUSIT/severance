package com.weavus.weavusys.workSchedule.controller;


import com.weavus.weavusys.workSchedule.entity.WorkSchedule;
import com.weavus.weavusys.workSchedule.service.WorkScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/workSchedule")
@RequiredArgsConstructor
public class WorkScheduleController {
    private final WorkScheduleService workScheduleService;

    @GetMapping("/{employeeId}/{year}/{month}")
    public List<WorkSchedule> getWorkSchedule(@PathVariable Long employeeId,
                                              @PathVariable int year,
                                              @PathVariable int month
    ) {
        List<WorkSchedule> workSchedules = workScheduleService.getWorkSchedule(employeeId, year, month);
        return workSchedules;
    }
}
