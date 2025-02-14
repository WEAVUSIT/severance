package com.weavus.weavusys.workSchedule.dto;

import com.weavus.weavusys.workSchedule.entity.EmployeeWorkDate;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Data
@RequiredArgsConstructor
public class EmployeeWorkDateDTO {
    private String employeeId; // 사원 ID
    private String employeeName; // 사원 이름
    private LocalTime checkInTime; // 출근 시간
    private LocalTime checkOutTime; // 퇴근 시간
    private Long basicWorkTime; //현장 기본 근무 시간
    private String workLocation; //근무지
    private LocalTime breakTimeIn; // 휴게시간 시작
    private LocalTime breakTimeOut; // 휴게시간 종료

    public EmployeeWorkDateDTO(String employeeName) {
        this.employeeName = employeeName;
    }

    public static EmployeeWorkDateDTO toDTP(EmployeeWorkDate employeeWorkDate) {

        EmployeeWorkDateDTO dto = new EmployeeWorkDateDTO();
        dto.employeeName = employeeWorkDate.getEmployee().getName();
        dto.employeeId = employeeWorkDate.getEmployee().getId();
        dto.checkInTime = employeeWorkDate.getCheckInTime();
        dto.checkOutTime = employeeWorkDate.getCheckOutTime();
        dto.breakTimeIn = employeeWorkDate.getBreakTimeIn();
        dto.breakTimeOut = employeeWorkDate.getBreakTimeOut();
        dto.basicWorkTime = employeeWorkDate.getBasicWorkTime();
        dto.workLocation = employeeWorkDate.getWorkLocation();

        return dto;
    }
}
