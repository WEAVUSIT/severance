package com.weavusys.personnel.service;


import com.weavusys.personnel.dto.InstitutionDTO;
import com.weavusys.personnel.dto.InstitutionDetailsDTO;
import com.weavusys.personnel.entity.Applicant;
import com.weavusys.personnel.entity.Institution;
import com.weavusys.personnel.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class InstitutionService {
    private final InstitutionRepository institutionRepository;

    public List<InstitutionDTO> getAllInstitutions() {
        return institutionRepository.findAll().stream().map(institution -> {
            InstitutionDTO dto = new InstitutionDTO();
            dto.setName(institution.getName());
            dto.setTotalQuota(institution.getTotalQuota());
            dto.setAdmittedCount(institution.getAdmittedCount());
            return dto;
        }).sorted((i1, i2) -> Integer.compare(i2.getTotalQuota(), i1.getTotalQuota())).collect(Collectors.toList());
    }

    public InstitutionDetailsDTO getInstitutionDetails(Long id) {
        Institution institution = institutionRepository.findById(id).orElseThrow();
        InstitutionDetailsDTO dto = new InstitutionDetailsDTO();
        dto.setName(institution.getName());
        dto.setContactInfo(institution.getContactInfo());
        dto.setApplicantNames(institution.getApplicants().stream().map(Applicant::getName).collect(Collectors.toList()));
        dto.setSchedules(List.of("Schedule 1", "Schedule 2")); // 예시 일정
        return dto;
    }
}
