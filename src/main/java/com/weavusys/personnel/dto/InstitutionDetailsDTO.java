package com.weavusys.personnel.dto;

import lombok.Data;

import java.util.List;

@Data
public class InstitutionDetailsDTO {
    private String name;
    private String contactInfo;
    private List<String> applicantNames;
    private List<String> schedules; // 일정 리스트
}
