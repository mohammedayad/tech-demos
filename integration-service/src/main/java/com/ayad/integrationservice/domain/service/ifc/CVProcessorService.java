package com.ayad.integrationservice.domain.service.ifc;

import com.ayad.integrationservice.domain.model.dtos.ProcessingResult;

import org.springframework.web.multipart.MultipartFile;

public interface CVProcessorService {

    String submitCV(MultipartFile cv);


    ProcessingResult getProcessingResult(String processId);
}
