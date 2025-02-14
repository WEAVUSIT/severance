package com.weavus.weavusys.workSchedule.controller;


import com.weavus.weavusys.workSchedule.dto.EmployeeWorkDateDTO;
import com.weavus.weavusys.workSchedule.dto.WorkScheduleDTO;
import com.weavus.weavusys.workSchedule.entity.WorkSchedule;
import com.weavus.weavusys.workSchedule.service.WorkScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/workSchedule")
@RequiredArgsConstructor
public class WorkScheduleController {
    private final WorkScheduleService workScheduleService;
    @GetMapping("/{username}/{year}/{month}")
    public List<WorkSchedule> getWorkSchedule(@PathVariable String username,
                                              @PathVariable int year,
                                              @PathVariable int month) {
        List<WorkSchedule> workScheduleList = workScheduleService.getWorkSchedule(username, year, month);
        return workScheduleList;
    }
    //EmployeeWorkDate 로그인한 사람의 정보 불러오기
    @GetMapping("/default")
    public EmployeeWorkDateDTO getDefaultWorkData() {
        return workScheduleService.getDefaultWorkData();
    }

    //EmployeeWorkDateDTO 저장
    @PostMapping("/default/save")
    public ResponseEntity saveDefaultWorkData(@RequestBody EmployeeWorkDateDTO employeeWorkDateDTO) {
        return workScheduleService.saveDefaultWorkData(employeeWorkDateDTO);
    }


    //workschedule 저장
    @PostMapping("/save")
    public ResponseEntity saveWorkSchedule(@RequestBody WorkScheduleDTO workScheduleDTO) {
        return workScheduleService.saveWorkSchedule(workScheduleDTO);
    }

    //workschedule 삭제
    @DeleteMapping("/delete/{id}")
    public ResponseEntity deleteWorkSchedule(@PathVariable Long id) {
        return workScheduleService.deleteWorkSchedule(id);
    }

    //EmployeeWorkDateDTO 수정
    @PutMapping("/default/update")
    public ResponseEntity updateDefaultWorkData(@RequestBody EmployeeWorkDateDTO employeeWorkDateDTO) {
        return workScheduleService.updateDefaultWorkData(employeeWorkDateDTO);
    }
}
