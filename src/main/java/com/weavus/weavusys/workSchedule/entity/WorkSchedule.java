package com.weavus.weavusys.workSchedule.entity;

import com.weavus.weavusys.calcul.entity.Employee;
import jakarta.persistence.*;
import lombok.*;

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
    @Column(nullable = false)
    private LocalDate workDate; // 출근 날짜
    @ManyToOne
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee; // 사원 ID (employees 테이블과 조인)
    @Column(nullable = false)
    private LocalTime checkInTime; // 출근 시간
    @Column(nullable = false)
    private LocalDate checkOutDate; // 퇴근 날짜 (야근 고려)
    @Column(nullable = false)
    private LocalTime checkOutTime; // 퇴근 시간

//    @ManyToOne
//    @JoinColumn(name = "workInfo_id", nullable = false)
//    private WorkInfo workInfo; // 현장 정보

    @Column(nullable = false)
    private boolean isHoliday; // 휴가 여부
    @Column
    private String log; // 비고
    @Column(nullable = false)
    private LocalTime brakeTimeIn; // 휴게시간
    @Column(nullable = false)
    private LocalTime brakeTimeOut; // 휴게시간
    @Column(nullable = false)
    private String workType; // 근무 유형
    @Column(nullable = false)
    private String workLocation; // 근무지
    //출근시간, 퇴근시간, 휴가 시간 고정으로 수정 가능하게,
    //마이페이지 -> 이름, 아이디,-< 변동 안되게 출근, 퇴근, 휴게시간 입력으로 디폴트 값
}
