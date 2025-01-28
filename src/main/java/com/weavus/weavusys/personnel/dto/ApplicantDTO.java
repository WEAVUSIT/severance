package com.weavus.weavusys.personnel.dto;

import com.weavus.weavusys.personnel.entity.Applicant;
import com.weavus.weavusys.personnel.entity.ApplicantFile;
import com.weavus.weavusys.personnel.entity.Institution;
import com.weavus.weavusys.personnel.repository.ApplicantFileRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Data
@RequiredArgsConstructor
public class ApplicantDTO {

    private static ApplicantFileRepository applicantFileRepository;

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

    //이력서 파일 저장
    private String resume1Path;
    private String resumeFileName1;
    private String resume2Path;
    private String resumeFileName2;
    private String resume3Path;
    private String resumeFileName3;

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



        //ApplicantService에 있는 메소드 extractOriginalFileName사용
        //null이 아닌경우에만 파일이름을 추출
//        String originalFileName1 = null;
//        String originalFileName2 = null;
//        String originalFileName3 = null;
//
//        if (applicant.getResumeFileName1() != null) {
//            String fileName1 = applicant.getResumeFileName1();
//             originalFileName1 = ApplicantService.extractOriginalFileName(fileName1);
//        }
//        if (applicant.getResumeFileName2() != null) {
//            String fileName2 = applicant.getResumeFileName2();
//             originalFileName2 = ApplicantService.extractOriginalFileName(fileName2);
//        }
//        if (applicant.getResumeFileName3() != null) {
//            String fileName3 = applicant.getResumeFileName3();
//             originalFileName3 = ApplicantService.extractOriginalFileName(fileName3);
//        }

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
//        dto.setResume1Path(applicant.getResume1Path());
//        dto.setResume2Path(applicant.getResume2Path());
//        dto.setResume3Path(applicant.getResume3Path());
//        dto.setResumeFileName1(originalFileName1);
//        dto.setResumeFileName2(originalFileName2);
//        dto.setResumeFileName3(originalFileName3);

        return dto;
    }
}
