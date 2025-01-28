package com.weavus.weavusys.personnel.controller;

import com.weavus.weavusys.personnel.dto.InstitutionDTO;
import com.weavus.weavusys.personnel.dto.InstitutionDetailsDTO;
import com.weavus.weavusys.personnel.entity.Institution;
import com.weavus.weavusys.personnel.service.InstitutionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    //기관 정보를 저장하는 메소드
    @PostMapping("/institution/add")
    public Institution addInstitution(@RequestBody InstitutionDTO institutionDTO) {
        return institutionService.addInstitution(institutionDTO);
    }

    //기관 정보 수정 메소드
    @PutMapping("/institution/{id}")
    public ResponseEntity updateInstitution(@PathVariable Long id, @RequestBody InstitutionDTO institutionDTO) {
        return institutionService.updateInstitution(id, institutionDTO);
    }

    //기관 정보 삭제 메소드
    @DeleteMapping("/institution/{id}")
    public ResponseEntity deleteInstitution(@PathVariable Long id) {
        return institutionService.deleteInstitution(id);
    }
}
