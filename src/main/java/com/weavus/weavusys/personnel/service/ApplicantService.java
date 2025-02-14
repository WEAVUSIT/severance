package com.weavus.weavusys.personnel.service;

import com.weavus.weavusys.enums.AdmissionStatus;
import com.weavus.weavusys.enums.Gender;
import com.weavus.weavusys.enums.VisaStatus;
import com.weavus.weavusys.personnel.dto.ApplicantDTO;
import com.weavus.weavusys.personnel.entity.Applicant;
import com.weavus.weavusys.personnel.entity.ApplicantFile;
import com.weavus.weavusys.personnel.entity.Institution;
import com.weavus.weavusys.personnel.repository.ApplicantFileRepository;
import com.weavus.weavusys.personnel.repository.ApplicantRepository;
import com.weavus.weavusys.personnel.repository.InstitutionRepository;
import lombok.RequiredArgsConstructor;
import net.lingala.zip4j.ZipFile;
import net.lingala.zip4j.model.ZipParameters;
import net.lingala.zip4j.model.enums.CompressionLevel;
import net.lingala.zip4j.model.enums.CompressionMethod;
import net.lingala.zip4j.model.enums.EncryptionMethod;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class ApplicantService {
    private final ApplicantRepository applicantRepository;
    private final InstitutionRepository institutionRepository;
    private final ApplicantFileRepository applicantFileRepository;
    private static final String upload_Dir = "C:/test";

    public List<ApplicantDTO> getAllApplicants() {
        return applicantRepository.findAll().stream()
                .map(ApplicantDTO::toDTO)
                .collect(Collectors.toList());
    }

    public ApplicantDTO getApplicantDetails(Long id) {
        //개별적인 id를 가지고 유저 정보를 취득한다 dto변환은 toDTO사용
        Applicant applicant = applicantRepository.findById(id).orElseThrow();
        ApplicantDTO applicantDTO = ApplicantDTO.toDTO(applicant);
        List<ApplicantFile> applicantFiles = applicantFileRepository.findByApplicantId(id);
        applicantDTO.setApplicantFile(applicantFiles != null ? applicantFiles : List.of());

        return applicantDTO;
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
            applicant.setLog(applicantDTO.getLog());
            Institution newInstitution = institutionRepository.findById(applicantDTO.getInstitutionId()).orElse(null);
            if(newInstitution != null) {
                applicant.setInstitution(newInstitution);
            } else {
                throw new IllegalArgumentException("Institution with id " + applicantDTO.getInstitution().getId() + " not found");
            }

            return applicantRepository.save(applicant);
        }
        return null;
    }

    @Transactional
    public String deleteApplicant(Long id) {
        try{
            applicantFileRepository.deleteByApplicantId(id);
            applicantRepository.deleteById(id);
            return "Applicant deleted successfully";
        } catch (Exception e) {
            throw new IllegalArgumentException("Applicant not found with id: " + id + e);
        }
    }


    @Transactional
    public ResponseEntity<List<ApplicantFile>> uploadResumes(Long id, List<MultipartFile> files, List<String> resumeTypes) {
    // 지원자 확인
    Applicant applicant = applicantRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Applicant not found with id: " + id));

        // 로컬 파일 저장 경로 C:\Data로 설정
        String uploadDir = "C:/Data";

        // 파일 저장
    for (int i = 0; i < files.size(); i++) {
        MultipartFile file = files.get(i);
        String resumeType = resumeTypes.get(i);
        applicantFileRepository.findByApplicantIdAndResumeType(id, resumeType)
                .ifPresent(applicantFileRepository::delete);

        try {
            String fileName = file.getOriginalFilename();
            File localFile = new File(uploadDir + File.separator + fileName);
            Files.createDirectories(Paths.get(uploadDir));  // 디렉토리 생성 (없으면)

            // 파일을 로컬 시스템에 저장
            file.transferTo(localFile);

            ApplicantFile applicantFile = ApplicantFile.builder()
                    .applicant(applicant)
                    .fileName(file.getOriginalFilename())
                    .fileType(file.getContentType())
                    .filePath(localFile.getAbsolutePath())  // 파일 경로 저장
//                    .fileData(file.getBytes())
                    .resumeType(resumeType)
                    .createdAt(LocalDateTime.now())
                    .build();
            applicantFileRepository.save(applicantFile);
        } catch (IOException e) {
            throw new RuntimeException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }
        List<ApplicantFile> updatedFiles  = applicantFileRepository.findByApplicantIdOrderByCreatedAt(id);
        if (updatedFiles.size() > 3) {
            // 초과 파일 삭제 (예: 오래된 파일 삭제)
            List<ApplicantFile> filesToRemove = updatedFiles.subList(3, updatedFiles.size());
            applicantFileRepository.deleteAll(filesToRemove);
            updatedFiles = updatedFiles.subList(0, 3); // 삭제 후 최신 리스트 다시 설정
        }

    return ResponseEntity.ok(updatedFiles);
}

        public ResponseEntity<Resource> downloadResumes(Long id) {
            List<ApplicantFile> applicantFiles = applicantFileRepository.findByApplicantId(id);
            Applicant applicantDTO = applicantRepository.findById(id).orElseThrow();
            if (applicantFiles.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            try {
                // 임시 ZIP 파일 생성
                File zipFile = File.createTempFile(applicantDTO.getName() + "의 이력서", ".zip");

                // ZIP 파일 암호화
                String password = "weavus1234";
                ZipParameters zipParameters = new ZipParameters();
                zipParameters.setCompressionMethod(CompressionMethod.DEFLATE);
                zipParameters.setCompressionLevel(CompressionLevel.NORMAL);
                zipParameters.setEncryptFiles(true);
                zipParameters.setEncryptionMethod(EncryptionMethod.ZIP_STANDARD);

                // 압축 파일 생성 및 파일 추가
                ZipFile secureZip = new ZipFile(zipFile, password.toCharArray());
                for (ApplicantFile applicantFile : applicantFiles) {
                    String originalFileName = applicantFile.getFileName(); // 저장된 원래 파일 이름 가져오기
//                    File tempFile = new File(System.getProperty("java.io.tmpdir"), originalFileName); // 원래 이름 사용
                    String filePath = applicantFile.getFilePath();
                    File fileToZip = new File(filePath, originalFileName);
//                    try (FileOutputStream fos = new FileOutputStream(tempFile)) {
//                        fos.write(applicantFile.getFileData());
//                          tempFile.delete(); // 임시 파일 삭제
//                    }
                    if (fileToZip.exists() && fileToZip.isFile()) {
                        secureZip.addFile(fileToZip, zipParameters);
                    }
                }


                // ZIP 파일을 클라이언트로 전송
                Resource resource = new ByteArrayResource(java.nio.file.Files.readAllBytes(zipFile.toPath()));

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resumes_" + id + ".zip\"")
                        .body(resource);

            } catch (Exception e) {
                throw new RuntimeException("Failed to create ZIP file", e);
            }

        }

        //파일 삭제 기능 파일 id값을 받아오면 삭제
        public ResponseEntity deleteFile(Long id) {
            applicantFileRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }

}
