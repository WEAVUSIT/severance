package com.weavusys.personnel.service;

import com.weavusys.personnel.dto.ApplicantDTO;
import com.weavusys.personnel.dto.InstitutionDetailsDTO;
import com.weavusys.personnel.entity.Applicant;
import com.weavusys.personnel.entity.Institution;
import com.weavusys.personnel.repository.ApplicantRepository;
import com.weavusys.personnel.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final InstitutionRepository institutionRepository;

    public List<ApplicantDTO> getAllApplicants() {
        return applicantRepository.findAll().stream().map(applicant -> {
            ApplicantDTO dto = new ApplicantDTO();
            dto.setName(applicant.getName());
            dto.setEducationInstitution(applicant.getEducationInstitution());
            dto.setVisaStatus(applicant.getVisaStatus());
            dto.setOfferStatus(applicant.getOfferStatus());
            dto.setJoiningDate(applicant.getJoiningDate());
            dto.setAdmissionStatus(applicant.getAdmissionStatus());
            return dto;
        }).sorted((a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName())).collect(Collectors.toList());
    }

    public ApplicantDTO getApplicantDetails(Long id) {
        return null;
    }

    public Applicant addApplicant(ApplicantDTO applicantDTO) {
        Institution institution = institutionRepository.findByName(applicantDTO.getEducationInstitution())
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));

        Applicant applicant = new Applicant();
        applicant.setName(applicantDTO.getName());
        applicant.setEducationInstitution(applicantDTO.getEducationInstitution());
        applicant.setAdmissionStatus("0"); // 기본값: 대기중
        applicant.setOfferStatus("0"); // 기본값: 내정예정
        applicant.setVisaStatus("0"); // 기본값: 서류 준비중
        applicant.setJoiningDate(applicantDTO.getJoiningDate());
        applicant.setInstitution(institution);

        institution.setTotalQuota(institution.getTotalQuota() + 1); // 총 지원자 수 증가
        institutionRepository.save(institution);

        return applicantRepository.save(applicant);
    }

    public Applicant updateApplicant(Long id, ApplicantDTO applicantDTO) {
        return null;
    }
}
