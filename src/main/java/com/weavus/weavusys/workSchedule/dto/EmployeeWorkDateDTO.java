package com.weavus.weavusys.workSchedule.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalTime;

@Data
@RequiredArgsConstructor
public class EmployeeWorkDateDTO {
    private String employeeId; // 사원 ID
    private LocalTime checkInTime; // 출근 시간
    private LocalTime checkOutTime; // 퇴근 시간
    private Long basicWorkTime; //현장 기본 근무 시간
    private String workLocation; //근무지
    private LocalTime breakTimeIn; // 휴게시간 시작
    private LocalTime breakTimeOut; // 휴게시간 종료
}
