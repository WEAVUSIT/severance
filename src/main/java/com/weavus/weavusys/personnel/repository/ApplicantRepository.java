package com.weavus.weavusys.personnel.repository;

import com.weavus.weavusys.personnel.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    boolean existsByName(String name);

    List<Applicant> findByInstitutionId(Long id);
}
