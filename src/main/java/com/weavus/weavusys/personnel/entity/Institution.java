package com.weavus.weavusys.personnel.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

    @Entity
    @Data
    @NoArgsConstructor
    public class Institution {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        @Column(nullable = false)
        private String name;
        @Column(nullable = false)
        private String managerName; //담당자 이름
        @Column(nullable = false)
        private String position; //직급
        @Column(nullable = false)
        private String contactInfo; //연락처
        private String log; // 상태 변경 이력 및 메모


    }
