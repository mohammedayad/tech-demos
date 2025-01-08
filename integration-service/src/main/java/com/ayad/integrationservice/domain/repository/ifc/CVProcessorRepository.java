package com.ayad.integrationservice.domain.repository.ifc;

import com.ayad.integrationservice.domain.model.entities.CvProcessorJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CVProcessorRepository extends JpaRepository<CvProcessorJob, String> {
}
