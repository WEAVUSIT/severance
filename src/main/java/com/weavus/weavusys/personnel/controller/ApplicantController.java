package com.weavus.weavusys.personnel.controller;


import com.weavus.weavusys.personnel.dto.ApplicantDTO;
import com.weavus.weavusys.personnel.entity.Applicant;
import com.weavus.weavusys.personnel.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
public class ApplicantController {
    private final ApplicantService applicantService;

    @GetMapping("/applicant/list")
    public List<ApplicantDTO> getAllApplicants() {
        return applicantService.getAllApplicants();
    }

    @GetMapping("/applicant/{id}")
    public ApplicantDTO getInstitutionDetails(@PathVariable Long id) {
        return applicantService.getApplicantDetails(id);
    }

    @PostMapping("/applicant/add")
    public String addApplicant(@RequestBody ApplicantDTO applicantDTO) {
        return applicantService.addApplicant(applicantDTO);
    }

    @PutMapping("/applicant/{id}")
    public ResponseEntity<Applicant> updateApplicant(@PathVariable Long id, @RequestBody ApplicantDTO applicantDTO) {
        Applicant updatedApplicant = applicantService.updateApplicant(id, applicantDTO);
        return updatedApplicant != null ? ResponseEntity.ok(updatedApplicant) : ResponseEntity.notFound().build();
    }

    //지원자 삭제
    @DeleteMapping("/applicant/{id}")
    public String deleteApplicant(@PathVariable Long id) {
        return applicantService.deleteApplicant(id);
    }

    //지원자 이력서 업로드
    @PostMapping("/applicant/{id}/upload")
    public ResponseEntity<Applicant> uploadResume(@PathVariable Long id,
                               @RequestParam("files") List<MultipartFile> files,
                               @RequestParam("resumeTypes") List<String> resumeTypes
    ) {
        return applicantService.uploadResumes(id, files, resumeTypes);
    }

    //지원자 이력서 다운로드
    @GetMapping("/applicant/{id}/download")
    public ResponseEntity<ByteArrayResource> downloadResume(@PathVariable Long id
                                                            ) {
        return applicantService.downloadResumes(id);
    }
}
