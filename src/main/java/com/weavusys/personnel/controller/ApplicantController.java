package com.weavusys.personnel.controller;


import com.weavusys.personnel.dto.ApplicantDTO;
import com.weavusys.personnel.dto.InstitutionDetailsDTO;
import com.weavusys.personnel.entity.Applicant;
import com.weavusys.personnel.service.ApplicantService;
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
    public Applicant addApplicant(@RequestBody ApplicantDTO applicantDTO) {
        return applicantService.addApplicant(applicantDTO);
    }

    @PutMapping("/applicant/{id}")
    public Applicant updateApplicant(@PathVariable Long id, @RequestBody ApplicantDTO applicantDTO) {
        return applicantService.updateApplicant(id, applicantDTO);
    }
}
