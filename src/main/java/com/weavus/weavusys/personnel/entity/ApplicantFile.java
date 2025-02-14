package com.weavus.weavusys.personnel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(name = "applicant_files")
public class ApplicantFile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "applicant_id", nullable = false)
    private Applicant applicant;
    @Column(name = "file_name", nullable = false)
    private String fileName;
    @Column(name = "file_type")
    private String fileType;
//    @Lob
//    @Column(name = "file_data", nullable = false, columnDefinition = "LONGBLOB")
//    private byte[] fileData;
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    @Column(name = "filePath")
    private String filePath;
    @Column(name="resumeType", nullable = false)
    private String resumeType;
}
