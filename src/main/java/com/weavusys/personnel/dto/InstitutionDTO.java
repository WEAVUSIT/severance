package com.weavusys.personnel.dto;

import lombok.Data;

@Data
public class InstitutionDTO {
    private String name;
    private int totalQuota;
    private int admittedCount;
}
