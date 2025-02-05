package com.weavus.weavusys.workSchedule.dto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@RequiredArgsConstructor
public class WorkScheduleDTO {
    private Long id;
    private String employeeId;
    private LocalDate checkInDate;
    private LocalTime checkInTime;
    private LocalTime checkOutTime;
    private LocalDate checkOutDate;
    private LocalTime breakTimeIn;
    private LocalTime breakTimeOut;
    private int isHoliday;
    private String memo;
    private String workType;
    private String workLocation;
    public String workPosition;
}
