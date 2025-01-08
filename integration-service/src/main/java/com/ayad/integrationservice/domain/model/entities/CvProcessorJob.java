package com.ayad.integrationservice.domain.model.entities;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "cv_processor_job")
public class CvProcessorJob {

    @Id
    private String processId;

    @Enumerated(EnumType.STRING)  // Persist the enum as a string in the database
    private Status status;         // Use the Status enum for job status

    private String filePath;  // Reference to the file in storage (e.g., URL or file path)

    @Lob
    private String result;  // Store the result or XML string if needed

    @CreationTimestamp
    private Instant creationDate;

    @UpdateTimestamp
    private Instant lastUpdate;
}
