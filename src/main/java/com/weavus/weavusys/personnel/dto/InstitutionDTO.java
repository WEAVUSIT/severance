package com.weavus.weavusys.personnel.dto;

import com.weavus.weavusys.personnel.entity.Institution;
import lombok.Data;

import java.util.List;

@Data
public class InstitutionDTO {
    private Long id;
    private String name;
    private String contactInfo;
    private String log; // 상태 변경 이력 및 메모
    private String managerName; //담당자 이름
    private String position; //직급
    private List<ApplicantDTO> applicants; // 지원자 리스트

    public static InstitutionDTO convertToDTO(Institution institution) {

        InstitutionDTO dto = new InstitutionDTO();
        dto.setId(institution.getId());
        dto.setName(institution.getName());
        dto.setContactInfo(institution.getContactInfo());
        dto.setLog(institution.getLog());
        dto.setManagerName(institution.getManagerName());
        dto.setPosition(institution.getPosition());

        return dto;
    }
}
