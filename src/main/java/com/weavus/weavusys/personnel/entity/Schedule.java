package com.weavus.weavusys.personnel.entity;

import com.weavus.weavusys.personnel.dto.ScheduleDTO;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String scheduleInfo;
    @Column(nullable = false)
    private LocalDateTime startDateTime;
    @Column(nullable = false)
    private LocalDateTime endDateTime;

    //어느 기관의 스케쥴인지 조인
    @ManyToOne
    @JoinColumn(name = "institution_id", nullable = false)
    private Institution institution;

    public static Schedule fromSchedule(ScheduleDTO scheduleDTO, Institution institution) {
        Schedule schedule = new Schedule();
        schedule.setName(scheduleDTO.getName());
        schedule.setScheduleInfo(scheduleDTO.getScheduleInfo());
        schedule.setStartDateTime(scheduleDTO.getStartDateTime());
        schedule.setEndDateTime(scheduleDTO.getEndDateTime());
        schedule.setInstitution(institution);
        return schedule;
    }
}
