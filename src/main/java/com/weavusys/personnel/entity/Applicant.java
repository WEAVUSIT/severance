package com.weavusys.personnel.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

    @Entity
    @Data
    @NoArgsConstructor
    public class Applicant {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;  //이름(영어로)
        private LocalDate joiningDate; //입사예정일
        private String admissionStatus; // 합격 여부
        private String offerStatus; // 내정 상태
        private LocalDate visaApplicationDate; // 비자 신청일
        private String visaStatus; // 비자 상태
        private String educationInstitution; // 소속 교육기관

        @ManyToOne
        @JoinColumn(name = "institution_id")
        private Institution institution; // 소속 교육기관 조인
    }
