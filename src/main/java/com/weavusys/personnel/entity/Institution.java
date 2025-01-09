package com.weavusys.personnel.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

    @Entity
    @Data
    @NoArgsConstructor
    public class Institution {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;
        private String contactInfo;
        private int totalQuota;
        private int admittedCount;

        @OneToMany(mappedBy = "institution", cascade = CascadeType.ALL)
        private List<Applicant> applicants;
    }
