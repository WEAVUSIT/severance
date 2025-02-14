package com.weavus.weavusys.workSchedule.service;

import com.weavus.weavusys.calcul.entity.Employee;
import com.weavus.weavusys.calcul.repo.EmployeeRepository;
import com.weavus.weavusys.enums.WorkSchedulePosition;
import com.weavus.weavusys.enums.WorkScheduleType;
import com.weavus.weavusys.workSchedule.dto.EmployeeWorkDateDTO;
import com.weavus.weavusys.workSchedule.dto.WorkScheduleDTO;
import com.weavus.weavusys.workSchedule.entity.EmployeeWorkDate;
import com.weavus.weavusys.workSchedule.entity.WorkSchedule;
import com.weavus.weavusys.workSchedule.repo.EmployeeWorkDateRepo;
import com.weavus.weavusys.workSchedule.repo.WorkScheduleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WorkScheduleService {
    private final WorkScheduleRepo workScheduleRepo;
    private final EmployeeRepository employeeRepository;
    private final EmployeeWorkDateRepo employeeWorkDateRepo;
    String userID = null;

    public List<WorkSchedule> getWorkSchedule(String employeeId, int year, int month) {
        return workScheduleRepo.findByEmployeeAndWorkDateBetween(employeeId, year, month);
    }

    public List<WorkSchedule> getWorkScheduleList() {
        return workScheduleRepo.findAll();
    }


    public ResponseEntity saveWorkSchedule(WorkScheduleDTO workScheduleDTO) {
        Employee employee = getEmployeeData();

        if (workScheduleRepo.existsByEmployeeIdAndCheckInDate(employee.getId(), workScheduleDTO.getCheckInDate())) {
            Optional<WorkSchedule> workScheduleFind = workScheduleRepo.findById(workScheduleDTO.getId());
            WorkSchedule workSchedule = workScheduleFind.get();
            workSchedule.setCheckInDate(workScheduleDTO.getCheckInDate());
            workSchedule.setCheckInTime(workScheduleDTO.getCheckInTime());
            workSchedule.setCheckOutDate(workScheduleDTO.getCheckOutDate());
            workSchedule.setCheckOutTime(workScheduleDTO.getCheckOutTime());
            workSchedule.setBreakTimeIn(workScheduleDTO.getBreakTimeIn());
            workSchedule.setBreakTimeOut(workScheduleDTO.getBreakTimeOut());
            workSchedule.setMemo(workScheduleDTO.getMemo());
            workSchedule.setWorkType(WorkScheduleType.valueOf(workScheduleDTO.getWorkType()));
            workSchedule.setWorkLocation(workScheduleDTO.getWorkLocation());
            workSchedule.setWorkPosition(WorkSchedulePosition.valueOf(workScheduleDTO.getWorkPosition()));
            workScheduleRepo.save(workSchedule);
        } else {
            WorkSchedule workSchedule = WorkSchedule.builder()
                    .employee(employee)
                    .checkInDate(workScheduleDTO.getCheckInDate())
                    .checkInTime(workScheduleDTO.getCheckInTime())
                    .checkOutDate(workScheduleDTO.getCheckOutDate())
                    .checkOutTime(workScheduleDTO.getCheckOutTime())
                    .breakTimeIn(workScheduleDTO.getBreakTimeIn())
                    .breakTimeOut(workScheduleDTO.getBreakTimeOut())
                    .memo(workScheduleDTO.getMemo())
                    .workType(WorkScheduleType.valueOf(workScheduleDTO.getWorkType()))
                    .workLocation(workScheduleDTO.getWorkLocation())
                    .workPosition(WorkSchedulePosition.valueOf(workScheduleDTO.getWorkPosition()))
                    .build();
            workScheduleRepo.save(workSchedule);
        }
        return ResponseEntity.ok(workScheduleDTO);
    }

    public ResponseEntity saveDefaultWorkData(EmployeeWorkDateDTO employeeWorkDateDTO) {
        Employee employee = getEmployeeData();

        if (employeeWorkDateRepo.existsByEmployeeId(employee.getId())) {
           // response에 message 넣기 -> 이미 등록된 기본 근무 정보가 있습니다.
            Map<String, Object> response = new HashMap<>();
            response.put("message", "이미 등록된 기본 근무 정보가 있습니다.");
            response.put("data", employeeWorkDateDTO);
            return ResponseEntity.badRequest().body(response);
        }

        EmployeeWorkDate employeeWorkDate = EmployeeWorkDate.builder()
                .employee(employee)
                .checkInTime(employeeWorkDateDTO.getCheckInTime())
                .checkOutTime(employeeWorkDateDTO.getCheckOutTime())
                .basicWorkTime(employeeWorkDateDTO.getBasicWorkTime())
                .workLocation(employeeWorkDateDTO.getWorkLocation())
                .breakTimeIn(employeeWorkDateDTO.getBreakTimeIn())
                .breakTimeOut(employeeWorkDateDTO.getBreakTimeOut())
                .build();
        employeeWorkDateRepo.save(employeeWorkDate);
        return ResponseEntity.ok(employeeWorkDate);
    }

    public Employee getEmployeeData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            userID = ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        if (userID == null) {
            throw new IllegalStateException("로그인한 사용자 정보를 찾을 수 없습니다.");
        }
        Employee employee = employeeRepository.findById(userID)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자를 찾을 수 없습니다: " + userID + "관리자에게 문의해 주세요."));
        return employee;
    }

    public EmployeeWorkDateDTO getDefaultWorkData() {
        Employee employee = getEmployeeData();
        try {
            EmployeeWorkDate employeeWorkDate = employeeWorkDateRepo.findByEmployeeId(employee.getId());
            return EmployeeWorkDateDTO.toDTP(employeeWorkDate);
        } catch (Exception e) {
            return new EmployeeWorkDateDTO(employee.getName());
        }
    }

    public ResponseEntity deleteWorkSchedule(Long id) {
        try {
            workScheduleRepo.deleteById(id);
            return ResponseEntity.ok("WorkSchedule deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("WorkSchedule not found with id: " + id + e);
        }
    }

    public ResponseEntity updateDefaultWorkData(EmployeeWorkDateDTO employeeWorkDateDTO) {
        Employee employee = getEmployeeData();

        if(employeeWorkDateRepo.findByEmployeeId(employee.getId()) == null){
            EmployeeWorkDate employeeWorkDate = EmployeeWorkDate.builder()
                    .employee(employee)
                    .checkInTime(employeeWorkDateDTO.getCheckInTime())
                    .checkOutTime(employeeWorkDateDTO.getCheckOutTime())
                    .basicWorkTime(employeeWorkDateDTO.getBasicWorkTime())
                    .workLocation(employeeWorkDateDTO.getWorkLocation())
                    .breakTimeIn(employeeWorkDateDTO.getBreakTimeIn())
                    .breakTimeOut(employeeWorkDateDTO.getBreakTimeOut())
                    .build();
            employeeWorkDateRepo.save(employeeWorkDate);
            return ResponseEntity.ok(employeeWorkDate);
        } else {
            EmployeeWorkDate employeeWorkDate = employeeWorkDateRepo.findByEmployeeId(employee.getId());
            employeeWorkDate.setCheckInTime(employeeWorkDateDTO.getCheckInTime());
            employeeWorkDate.setCheckOutTime(employeeWorkDateDTO.getCheckOutTime());
            employeeWorkDate.setBasicWorkTime(employeeWorkDateDTO.getBasicWorkTime());
            employeeWorkDate.setWorkLocation(employeeWorkDateDTO.getWorkLocation());
            employeeWorkDate.setBreakTimeIn(employeeWorkDateDTO.getBreakTimeIn());
            employeeWorkDate.setBreakTimeOut(employeeWorkDateDTO.getBreakTimeOut());
            employeeWorkDateRepo.save(employeeWorkDate);
            return ResponseEntity.ok(employeeWorkDateDTO);
        }
    }
}
