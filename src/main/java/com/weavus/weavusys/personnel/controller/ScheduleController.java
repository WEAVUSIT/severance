package com.weavus.weavusys.personnel.controller;

import com.weavus.weavusys.personnel.dto.ScheduleDTO;
import com.weavus.weavusys.personnel.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
public class ScheduleController {
    private final InstitutionService institutionService;

    //기관 스케줄 작성 메소드 addSchedule사용
    @PostMapping("/institution/{id}/schedule/add")
    public ResponseEntity addSchedule(@PathVariable Long id, @RequestBody ScheduleDTO scheduleDTO) {
        return institutionService.addSchedule(id, scheduleDTO);
    }
    //기관 스케줄 수정 메소드 updateSchedule사용
    @PutMapping("/institution/{id}/schedule/{scheduleId}")
    public ResponseEntity updateSchedule(@PathVariable Long id, @PathVariable Long scheduleId, @RequestBody ScheduleDTO scheduleDTO) {
        return institutionService.updateSchedule(scheduleId, scheduleDTO);
    }

    //기관 스케줄 삭제 메소드 deleteSchedule사용
    @DeleteMapping("/institution/{id}/schedule/{scheduleId}")
    public ResponseEntity deleteSchedule(@PathVariable Long scheduleId) {
        return institutionService.deleteSchedule(scheduleId);
    }

    //기관 스케줄 디테일 메소드
    @GetMapping("/institution/{id}/schedule/{scheduleId}")
    public ScheduleDTO getScheduleDetails(@PathVariable Long id, @PathVariable Long scheduleId) {
        return institutionService.getScheduleDetails(scheduleId);
    }

    //각기관의 스케쥴 정보
    @GetMapping("/institution/schedule/list")
    public List<ScheduleDTO> getAllSchedules() {
        return institutionService.getAllSchedules();
    }
}
