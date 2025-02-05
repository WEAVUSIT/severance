package com.weavus.weavusys.workSchedule.service;

import com.weavus.weavusys.calcul.entity.Employee;
import com.weavus.weavusys.calcul.repo.EmployeeRepository;
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
    public List<WorkSchedule> getWorkSchedule(String employeeId, int year, int month) {
        return workScheduleRepo.findByEmployeeAndWorkDateBetween(employeeId, year, month);
    }

    public List<WorkSchedule> getWorkScheduleList() {
        return workScheduleRepo.findAll();
    }
    String userID = null;


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
            workSchedule.setWorkType(workScheduleDTO.getWorkType());
            workSchedule.setWorkLocation(workScheduleDTO.getWorkLocation());
            workSchedule.setWorkPosition(workScheduleDTO.getWorkPosition());
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
                    .isHoliday(workScheduleDTO.getIsHoliday())
                    .memo(workScheduleDTO.getMemo())
                    .workType(workScheduleDTO.getWorkType())
                    .workLocation(workScheduleDTO.getWorkLocation())
                    .workPosition(workScheduleDTO.getWorkPosition())
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
            ResponseEntity.badRequest().body(response);
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
}
