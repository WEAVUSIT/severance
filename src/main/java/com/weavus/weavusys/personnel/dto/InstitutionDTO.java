package com.weavus.weavusys.personnel.dto;

import com.weavus.weavusys.personnel.entity.Institution;
import lombok.Data;

@Data
public class InstitutionDTO {
    private String name;
    private String contactInfo;

    public static InstitutionDTO convertToDTO(Institution institution) {
        InstitutionDTO dto = new InstitutionDTO();
        dto.setName(institution.getName());
        dto.setContactInfo(institution.getContactInfo());
        return dto;
    }
}
