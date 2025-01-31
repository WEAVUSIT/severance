package com.weavus.weavusys.calcul.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonthLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;  //직급 고유값
    @Column(nullable = false)
    private Long monthlyTotal;
    @Column(nullable = false)
    private LocalDate saveDate;
}
