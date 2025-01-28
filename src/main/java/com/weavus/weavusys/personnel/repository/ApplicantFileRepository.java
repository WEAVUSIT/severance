package com.weavus.weavusys.personnel.repository;

import com.weavus.weavusys.personnel.entity.ApplicantFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ApplicantFileRepository extends JpaRepository<ApplicantFile, Long> {

    List<ApplicantFile> findByApplicantId(Long applicantId);

    Optional<ApplicantFile> findByApplicantIdAndResumeType(Long applicantId, String resumeType);

    List<ApplicantFile> findByApplicantIdOrderByCreatedAt(Long id);
}
