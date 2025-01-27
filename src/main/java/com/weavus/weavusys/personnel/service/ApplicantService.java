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
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final InstitutionRepository institutionRepository;
    private static final String upload_Dir = "C:/test";


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
            if (!applicant.getAdmissionStatus().name().equals(applicantDTO.getAdmissionStatus())) {
                applicant.setStatusDate(LocalDate.now());
            }
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

            return applicantRepository.save(applicant);
        }
        return null;
    }

    public String deleteApplicant(Long id) {
        applicantRepository.deleteById(id);
        return "Applicant deleted successfully";
    }

    public ResponseEntity<Applicant> uploadResumes(Long id, List<MultipartFile> files, List<String> resumeTypes) {
        Path uploadDir = Paths.get(upload_Dir);

        // 디렉토리 생성
        if (!Files.exists(uploadDir)) {
            try {
                Files.createDirectories(uploadDir);
            } catch (IOException e) {
                throw new IllegalArgumentException("파일 디렉토리 생성 실패");
            }
        }
        Applicant applicant = applicantRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Applicant not found")
        );

        if (files == null || files.isEmpty()) {
            throw new IllegalArgumentException("No files provided for upload");
        }

        try {
            for (int i = 0; i < files.size(); i++) {
                MultipartFile file = files.get(i);
                String resumeType = resumeTypes.get(i);
                // 파일 이름 검증 및 UUID 생성
                String originalFileName = file.getOriginalFilename();
                if (originalFileName == null || originalFileName.isBlank()) {
                    throw new IllegalArgumentException("Invalid file name");
                }
                String safeFileName = UUID.randomUUID().toString() + "_" + originalFileName;
                if(resumeType.equals("resumeFileName1")) {
                    applicant.setResume1Path(uploadDir.resolve(safeFileName).toString());
                    applicant.setResumeFileName1(safeFileName);
                } else if(resumeType.equals("resumeFileName2")) {
                    applicant.setResume2Path(uploadDir.resolve(safeFileName).toString());
                    applicant.setResumeFileName2(safeFileName);
                } else if(resumeType.equals("resumeFileName3")) {
                    applicant.setResume3Path(uploadDir.resolve(safeFileName).toString());
                    applicant.setResumeFileName3(safeFileName);
                } else {
                    throw new IllegalArgumentException("Invalid resume type");
                }

                // 파일 저장
                try {
                    Path filePath = uploadDir.resolve(safeFileName);
                    file.transferTo(filePath.toFile());  // 파일을 지정된 경로에 저장
                } catch (IOException e) {
                   // return "파일 업로드 실패: " + file.getOriginalFilename();
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(null);
                }

            }

            applicantRepository.save(applicant); // 업데이트된 정보를 저장
            return ResponseEntity.ok(applicant);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


    public ResponseEntity<ByteArrayResource> downloadResumes(Long id) {
        Applicant applicant = applicantRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Applicant not found"));

//        Path uploadPath = ; // Synology 드라이브 경로 설정
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        List<String> filesNotFound = new ArrayList<>(); // 파일이 없을 경우 저장할 리스트

        try (ZipArchiveOutputStream zipOutputStream = new ZipArchiveOutputStream(byteArrayOutputStream)) {
            for (int i = 0; i < 3; i++) {
                String filePath = "";
                String fileName = "";
                boolean fileExists = false;

                int resumeNumbers = i + 1;

                switch (resumeNumbers) {
                    case 1:
                        fileName = applicant.getResumeFileName1();
                        filePath = applicant.getResume1Path();
                        break;
                    case 2:
                        fileName = applicant.getResumeFileName2();
                        filePath = applicant.getResume2Path();
                        break;
                    case 3:
                        fileName = applicant.getResumeFileName3();
                        filePath = applicant.getResume3Path();
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid resume number");
                }

                if(filePath == null || filePath.isBlank()) {
                    continue; // 파일 경로가 없으면 다음 파일로 이동
                }

                Path pathToFile = Paths.get(filePath);
                if (Files.exists(pathToFile)) {
                    fileExists = true;

                    // UUID 제거 후 원래 파일 이름 추출
                    String originalFileName = extractOriginalFileName(fileName);

                    ZipArchiveEntry zipEntry = new ZipArchiveEntry(originalFileName);
                    zipOutputStream.putArchiveEntry(zipEntry);
                    Files.copy(pathToFile, zipOutputStream); // 파일을 ZIP 스트림에 복사
                    zipOutputStream.closeArchiveEntry();
                } else {
                    filesNotFound.add(fileName);
                }
//                if (filesNotFound.size() == 3) {
//                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//                            .body(null); // 파일이 없으면 404 반환
//                }
            }
            zipOutputStream.finish(); // ZIP 마감
            byte[] zipData = byteArrayOutputStream.toByteArray();
            ByteArrayResource resource = new ByteArrayResource(zipData);

            // 압축된 ZIP 파일 반환
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=resumes.zip")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(resource);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(null);
        }
    }


//   파일 이름에서 UUID를 제거하고 원래 파일 이름을 반환하는 메서드
    public static String extractOriginalFileName(String fileName) {
        int underscoreIndex = fileName.indexOf("_");
        if (underscoreIndex != -1) {
            return fileName.substring(underscoreIndex + 1); // "_" 이후의 문자열 반환
        }
        return fileName; // UUID가 없으면 원래 이름 반환
    }

}
