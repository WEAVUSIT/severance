package com.weavus.weavusys.personnel.dto;

import com.weavus.weavusys.personnel.entity.Applicant;
import com.weavus.weavusys.personnel.entity.ApplicantFile;
import com.weavus.weavusys.personnel.entity.Institution;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ApplicantDTO {

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
    private String log;

    //ApplicantDTO에 ApplicantFile 연결
    private List<ApplicantFile> applicantFile;


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
        dto.setLog(applicant.getLog());

        return dto;
    }
}
