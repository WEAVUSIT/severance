package com.weavus.weavusys.personnel.dto;

import com.weavus.weavusys.personnel.entity.Institution;
import lombok.Data;

@Data
public class InstitutionDTO {
    private Long id;
    private String name;
    private String contactInfo;
    private String log; // 상태 변경 이력 및 메모


    public static InstitutionDTO convertToDTO(Institution institution) {
        InstitutionDTO dto = new InstitutionDTO();
        dto.setId(institution.getId());
        dto.setName(institution.getName());
        dto.setContactInfo(institution.getContactInfo());
        return dto;
    }
}
