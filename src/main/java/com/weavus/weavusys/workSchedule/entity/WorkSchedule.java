package com.weavus.weavusys.workSchedule.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.weavus.weavusys.calcul.entity.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WorkSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee; // 사원 ID (employees 테이블과 조인)
    @Column(nullable = false)
    private LocalDate checkInDate; // 출근 날짜
    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkInTime; // 출근 시간
    private LocalDate checkOutDate; // 퇴근 날짜 (야근 고려)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime checkOutTime; // 퇴근 시간
    @Column(nullable = false)
    private int isHoliday; // 휴가 여부
    @Column
    private String memo; // 비고
    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakTimeIn; // 휴게시간
    @Column(nullable = false)
    @JsonFormat(pattern = "HH:mm")
    private LocalTime breakTimeOut; // 휴게시간
    @Column(nullable = false)
    private String workType; // 근무 유형 - 欠勤,代休,有給休暇,振替休日,特別休暇,慶弔休暇,休日出勤
    @Column(nullable = false)
    private String workLocation; // 근무지 - 현장 위치
    @Column(nullable = false)
    private String workPosition; //근무 위치 - 본사, 현장, 재택근무
    //출근시간, 퇴근시간, 휴가 시간 고정으로 수정 가능하게,
    //마이페이지 -> 이름, 아이디,-< 변동 안되게 출근, 퇴근, 휴게시간 입력으로 디폴트 값
}
