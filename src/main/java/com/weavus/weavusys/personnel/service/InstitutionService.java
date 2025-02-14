package com.weavus.weavusys.personnel.service;


import com.weavus.weavusys.enums.ScheduleType;
import com.weavus.weavusys.personnel.dto.ApplicantDTO;
import com.weavus.weavusys.personnel.dto.InstitutionDTO;
import com.weavus.weavusys.personnel.dto.InstitutionDetailsDTO;
import com.weavus.weavusys.personnel.dto.ScheduleDTO;
import com.weavus.weavusys.personnel.entity.Institution;
import com.weavus.weavusys.personnel.entity.Schedule;
import com.weavus.weavusys.personnel.repository.ApplicantRepository;
import com.weavus.weavusys.personnel.repository.InstitutionRepository;
import com.weavus.weavusys.personnel.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor

public class InstitutionService {
    private final InstitutionRepository institutionRepository;
    private final ScheduleRepository scheduleRepository;
    private final ApplicantRepository applicantRepository;

    public List<InstitutionDTO> getAllInstitutions() {

         List<InstitutionDTO> dto = institutionRepository.findAll().stream()
                 .map(InstitutionDTO::convertToDTO)
                 .collect(Collectors.toList());
         for (InstitutionDTO institutionDTO : dto) {
             institutionDTO.setApplicants(
                     applicantRepository.findByInstitutionId(institutionDTO.getId()) != null
                             ? applicantRepository.findByInstitutionId(institutionDTO.getId()).stream()
                             .map(ApplicantDTO::toDTO)
                             .collect(Collectors.toList())
                             : List.of()
             );
         }
        return dto;
    }


    public InstitutionDetailsDTO getInstitutionDetails(Long id) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Institution with id " + id + " not found"));

        InstitutionDetailsDTO dto = new InstitutionDetailsDTO();
        dto.setId(institution.getId());
        dto.setName(institution.getName());
        dto.setContactInfo(institution.getContactInfo());
        dto.setLog(institution.getLog());
        dto.setManagerName(institution.getManagerName());
        dto.setPosition(institution.getPosition());
        dto.setApplicants(
                applicantRepository.findByInstitutionId(id) != null
                        ? applicantRepository.findByInstitutionId(id).stream()
                        .map(ApplicantDTO::toDTO)
                        .collect(Collectors.toList())
                        : List.of()
        );

        dto.setSchedules(
                scheduleRepository.findByInstitutionId(id) != null
                        ? scheduleRepository.findByInstitutionId(id).stream()
                        .map(ScheduleDTO::ToScheduleDTO)
                        .collect(Collectors.toList())
                        : List.of()
        );
        return dto;
    }

    public Institution addInstitution(InstitutionDTO institutionDTO) {
        if (institutionRepository.existsByName(institutionDTO.getName())) {
            throw new IllegalArgumentException("Institution with name " + institutionDTO.getName() + " already exists");
        }

        Institution institution = new Institution();
        institution.setName(institutionDTO.getName());
        institution.setContactInfo(institutionDTO.getContactInfo());
        institution.setManagerName(institutionDTO.getManagerName());
        institution.setPosition(institutionDTO.getPosition());
        return institutionRepository.save(institution);
    }

    public ResponseEntity updateInstitution(Long id, InstitutionDTO institutionDTO) {

        try{
            Institution institution = institutionRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Institution not found"));
            institution.setName(institutionDTO.getName());
            institution.setContactInfo(institutionDTO.getContactInfo());
            institution.setLog(institutionDTO.getLog());
            institution.setManagerName(institutionDTO.getManagerName());
            institution.setPosition(institutionDTO.getPosition());
            institutionRepository.save(institution);
            return ResponseEntity.ok(institution);
        } catch (IllegalArgumentException e) {
            Map<String, String > error = new HashMap<>();
            error.put("error", "Invalid Request");
            error.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        }
    }

    //시업에 스케줄 추가
    public ResponseEntity addSchedule(Long institutionId, ScheduleDTO scheduleDTO) {
        Institution institution = institutionRepository.findById(institutionId)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));

        scheduleRepository.save(Schedule.fromSchedule(scheduleDTO, institution));

        return ResponseEntity.ok().build();
    }

    //스케줄 수정
    public ResponseEntity updateSchedule(Long scheduleId, ScheduleDTO scheduleDTO) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        schedule.setName(scheduleDTO.getName());
        schedule.setScheduleInfo(scheduleDTO.getScheduleInfo());
        schedule.setStartDateTime(scheduleDTO.getStartDateTime());
        schedule.setEndDateTime(scheduleDTO.getEndDateTime());
        schedule.setScheduleType(ScheduleType.valueOf(scheduleDTO.getScheduleType()));
        scheduleRepository.save(schedule);
        return ResponseEntity.ok().build();
    }

    public ResponseEntity deleteSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        scheduleRepository.delete(schedule);
        return ResponseEntity.ok().build();
    }

    public ScheduleDTO getScheduleDetails(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new IllegalArgumentException("Schedule not found"));

        return ScheduleDTO.ToScheduleDTO(schedule);
    }

    public List<ScheduleDTO> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(ScheduleDTO::ToScheduleDTO)
                .collect(Collectors.toList());
    }

    public ResponseEntity deleteInstitution(Long id) {
        Institution institution = institutionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Institution not found"));

        institutionRepository.delete(institution);
        return ResponseEntity.ok().build();
    }
}
