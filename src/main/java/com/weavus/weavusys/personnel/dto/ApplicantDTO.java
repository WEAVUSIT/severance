package com.weavus.weavusys.personnel.dto;

import com.weavus.weavusys.personnel.entity.Applicant;
import com.weavus.weavusys.personnel.entity.Institution;
import lombok.Data;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Data
public class ApplicantDTO {  //대소문자 확인하기

    private Long id;
    private String name;
    private String gender;
    private String email;
    private LocalDate birthDate;
    private String phoneNumber;
    private LocalDate joiningDate;
    private String admissionStatus;
    private LocalDate visaApplicationDate;
    private String visaStatus;
    private Institution institution;
    private Long institutionId;
    private Long statusDate;
    private byte[] resume1;
    private String resumeFileName1;
    private byte[] resume2;
    private String resumeFileName2;
    private byte[] resume3;
    private String resumeFileName3;

    public static ApplicantDTO toDTO(Applicant applicant) {

        long daysElapsed;
        //오늘날짜기준 applicant.getStatusDate()로부터 몇일 경과하였는지 계산
        //만약 statusdate가 없다면 joiningDate로부터로 비교
        if (applicant.getStatusDate() == null) {
            daysElapsed = ChronoUnit.DAYS.between(applicant.getJoiningDate(), LocalDate.now());
        }else {
            daysElapsed = ChronoUnit.DAYS.between(applicant.getStatusDate(), LocalDate.now());
        }

        ApplicantDTO dto = new ApplicantDTO();
        dto.setId(applicant.getId());
        dto.setName(applicant.getName());
        dto.setGender(applicant.getGender().getDisplayName());
        dto.setEmail(applicant.getEmail());
        dto.setBirthDate(applicant.getBirthDate());
        dto.setPhoneNumber(applicant.getPhoneNumber());
        dto.setJoiningDate(applicant.getJoiningDate());
        dto.setVisaApplicationDate(applicant.getVisaApplicationDate());
        dto.setAdmissionStatus(applicant.getAdmissionStatus().name());
        dto.setVisaStatus(applicant.getVisaStatus().name());
        dto.setInstitution(applicant.getInstitution());
        dto.setInstitutionId(applicant.getInstitution().getId());
        dto.setStatusDate(daysElapsed);
        dto.setResume1(applicant.getResume1());
        dto.setResumeFileName1(applicant.getResumeFileName1());
        dto.setResume2(applicant.getResume2());
        dto.setResumeFileName2(applicant.getResumeFileName2());
        dto.setResume3(applicant.getResume3());
        dto.setResumeFileName3(applicant.getResumeFileName3());

        return dto;
    }
}
