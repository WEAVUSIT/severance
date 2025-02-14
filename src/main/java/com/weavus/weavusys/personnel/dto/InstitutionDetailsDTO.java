package com.weavus.weavusys.personnel.dto;

import lombok.Data;

import java.util.List;

@Data
public class InstitutionDetailsDTO {
    private Long id;
    private String name;
    private String contactInfo;
    private String log; // 상태 변경 이력 및 메모
    private String managerName; //담당자 이름
    private String position; //직급
    private List<ApplicantDTO> applicants; // 지원자 리스트
    private List<ScheduleDTO> schedules; // 일정 리스트
}
