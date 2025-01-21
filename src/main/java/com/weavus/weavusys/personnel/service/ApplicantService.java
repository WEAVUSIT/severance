package com.weavus.weavusys.personnel.service;

import com.weavus.weavusys.enums.AdmissionStatus;
import com.weavus.weavusys.enums.Gender;
import com.weavus.weavusys.enums.VisaStatus;
import com.weavus.weavusys.personnel.dto.ApplicantDTO;
import com.weavus.weavusys.personnel.entity.Applicant;
import com.weavus.weavusys.personnel.entity.Institution;
import com.weavus.weavusys.personnel.repository.ApplicantRepository;
import com.weavus.weavusys.personnel.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final InstitutionRepository institutionRepository;

    public List<ApplicantDTO> getAllApplicants() {
        return applicantRepository.findAll().stream()
                .map(ApplicantDTO::toDTO)
                .collect(Collectors.toList());
    }

    public ApplicantDTO getApplicantDetails(Long id) {
        //개별적인 id를 가지고 유저 정보를 취득한다 dto변환은 toDTO사용
        Applicant applicant = applicantRepository.findById(id).orElseThrow();
        return ApplicantDTO.toDTO(applicant);
    }

    public String addApplicant(ApplicantDTO applicantDTO) {
        Institution institution = institutionRepository.findById(applicantDTO.getInstitutionId())
                .orElseThrow(
                () -> new IllegalArgumentException("Institution with id " + applicantDTO.getInstitutionId() + " not found")
        );

        Applicant applicant = Applicant.fromDTO(applicantDTO, institution);

        applicantRepository.save(applicant);
        return "Applicant added successfully";
    }

    @Transactional
    public Applicant updateApplicant(Long id, ApplicantDTO applicantDTO) {

        Optional<Applicant> applicantUp= applicantRepository.findById(id);
        if(applicantUp.isPresent()) {
            Gender gender = Gender.fromDisplayName(applicantDTO.getGender());

            Applicant applicant = applicantUp.get();
            applicant.setName(applicantDTO.getName());
            applicant.setGender(gender);
            applicant.setEmail(applicantDTO.getEmail());
            applicant.setBirthDate(applicantDTO.getBirthDate());
            applicant.setPhoneNumber(applicantDTO.getPhoneNumber());
            applicant.setJoiningDate(applicantDTO.getJoiningDate());
            applicant.setAdmissionStatus(AdmissionStatus.valueOf(applicantDTO.getAdmissionStatus()));
            applicant.setVisaApplicationDate(applicantDTO.getVisaApplicationDate());
            applicant.setVisaStatus(VisaStatus.valueOf(applicantDTO.getVisaStatus()));
            Institution newInstitution = institutionRepository.findById(applicantDTO.getInstitutionId()).orElse(null);
            if(newInstitution != null) {
                applicant.setInstitution(newInstitution);
            } else {
                throw new IllegalArgumentException("Institution with id " + applicantDTO.getInstitution().getId() + " not found");
            }
            //만약 AdmissionStatus가 변경되었다면 statusDate를 현재 날짜로 업데이트
            if (!applicant.getAdmissionStatus().equals(applicantDTO.getAdmissionStatus())) {
                applicant.setStatusDate(LocalDate.now());
            }
            return applicantRepository.save(applicant);
        }
        return null;
    }

    public String deleteApplicant(Long id) {
        applicantRepository.deleteById(id);
        return "Applicant deleted successfully";
    }
//파일다운로드 제작중
    public String uploadResume(Long id, MultipartFile multipartFile, int resumeNumber) {
        Applicant applicant = applicantRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Applicant not found")
        );

        try {
            switch (resumeNumber) {
                case 1:
                    applicant.setResume1(multipartFile.getBytes());
                    applicant.setResumeFileName1(multipartFile.getOriginalFilename());
                    break;
                case 2:
                    applicant.setResume2(multipartFile.getBytes());
                    applicant.setResumeFileName2(multipartFile.getOriginalFilename());
                    break;
                case 3:
                    applicant.setResume3(multipartFile.getBytes());
                    applicant.setResumeFileName3(multipartFile.getOriginalFilename());
                    break;
                default:
                    throw new IllegalArgumentException("Invalid resume number");
            }
            return "Resume uploaded successfully";
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload resume: " + e.getMessage());
        }

    }

    public ResponseEntity<ByteArrayResource> downloadResume(Long id, int resumeNumber) {
        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

        byte[] resumeData;
        String fileName;

        switch (resumeNumber) {
            case 1:
                resumeData = applicant.getResume1();
                fileName = applicant.getResumeFileName1();
                break;
            case 2:
                resumeData = applicant.getResume2();
                fileName = applicant.getResumeFileName2();
                break;
            case 3:
                resumeData = applicant.getResume3();
                fileName = applicant.getResumeFileName3();
                break;
            default:
                throw new IllegalArgumentException("Invalid resume number");
        }

        if (resumeData == null) {
            return ResponseEntity.notFound().build();
        }

            ByteArrayResource resource = new ByteArrayResource(resumeData);

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);
    }
}
