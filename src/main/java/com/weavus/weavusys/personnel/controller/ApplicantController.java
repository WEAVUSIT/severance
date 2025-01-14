package com.weavus.weavusys.personnel.controller;


import com.weavus.weavusys.personnel.dto.ApplicantDTO;
import com.weavus.weavusys.personnel.service.ApplicantService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public String addApplicant(@RequestBody ApplicantDTO applicantDTO, @RequestParam Long institutionId) {
        return applicantService.addApplicant(applicantDTO, institutionId);
    }

    @PutMapping("/applicant/{id}")
    public String updateApplicant(@PathVariable Long id, @RequestBody ApplicantDTO applicantDTO) {
        return applicantService.updateApplicant(id, applicantDTO);
    }
}
