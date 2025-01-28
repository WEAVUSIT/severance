package com.weavus.weavusys.personnel.repository;

import com.weavus.weavusys.personnel.entity.Applicant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicantRepository extends JpaRepository<Applicant, Long> {
    Optional<Applicant> findById(Long id);

    List<Applicant> findByInstitutionId(Long id);
}
