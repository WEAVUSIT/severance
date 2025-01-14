package com.weavus.weavusys.personnel.dto;

import com.weavus.weavusys.personnel.entity.Institution;
import com.weavus.weavusys.personnel.entity.Schedule;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ScheduleDTO {
    private String name;
    private String scheduleInfo;
    private LocalDateTime startDateTime;
    private LocalDateTime endDateTime;
    private Long institutionId;

    public static ScheduleDTO ToScheduleDTO(Schedule schedule) {

        ScheduleDTO dto = new ScheduleDTO();
        dto.setName(schedule.getName());
        dto.setScheduleInfo(schedule.getScheduleInfo());
        dto.setStartDateTime(schedule.getStartDateTime());
        dto.setEndDateTime(schedule.getEndDateTime());
        dto.setInstitutionId(schedule.getInstitution().getId());
        return dto;
    }
}
