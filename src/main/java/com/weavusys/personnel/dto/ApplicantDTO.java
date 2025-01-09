package com.weavusys.personnel.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ApplicantDTO {
    private String name;
    private String educationInstitution;
    private String visaStatus;
    private String offerStatus;
    private LocalDate joiningDate;
    private String admissionStatus;
}
