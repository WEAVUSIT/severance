package com.weavus.weavusys.personnel.dto;

import lombok.Data;

import java.util.List;

@Data
public class InstitutionDetailsDTO {
    private Long id;
    private String name;
    private String contactInfo;
    private String log; // 상태 변경 이력 및 메모

    private List<String> applicantNames;
    private List<ScheduleDTO> schedules; // 일정 리스트
}
