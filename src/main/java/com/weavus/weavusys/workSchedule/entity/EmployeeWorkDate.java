package com.weavus.weavusys.workSchedule.entity;

import com.weavus.weavusys.calcul.entity.Employee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeWorkDate {
    @ManyToOne
    @Id
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee; // 사원 ID (employees 테이블과 조인)
    @Column(nullable = false)
    private LocalTime checkInTime; // 출근 시간
    @Column(nullable = false)
    private LocalTime checkOutTime; // 퇴근 시간
    @Column(nullable = false)
    private Long basicWorkTime; //현장 기본 근무 시간
    @Column(nullable = false)
    private String workLocation; //근무지
    @Column(nullable = false)
    private LocalTime breakTimeIn; // 휴게시간 시작
    @Column(nullable = false)
    private LocalTime breakTimeOut; // 휴게시간 종료
}
