package com.weavus.weavusys.calcul.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Amount {

    @Id
    private Long rank;  //직급 고유값
    @Column(nullable = false)
    private Integer monthlyAmount;
}
