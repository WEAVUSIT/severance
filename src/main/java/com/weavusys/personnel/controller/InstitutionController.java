package com.weavusys.personnel.controller;

import com.weavusys.personnel.dto.InstitutionDTO;
import com.weavusys.personnel.dto.InstitutionDetailsDTO;
import com.weavusys.personnel.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
public class InstitutionController {
    private final InstitutionService institutionService;

    @GetMapping("/institution/list")
    public List<InstitutionDTO> getAllInstitutions() {
        return institutionService.getAllInstitutions();
    }

    @GetMapping("/institution/{id}")
    public InstitutionDetailsDTO getInstitutionDetails(@PathVariable Long id) {
        return institutionService.getInstitutionDetails(id);
    }
}
