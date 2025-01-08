package com.ayad.integrationservice.domain.service.impl;

import com.ayad.integrationservice.clients.proxy.ifc.TkExtractionClient;
import com.ayad.integrationservice.common.exception.ProcessIdNotFoundException;
import com.ayad.integrationservice.common.exception.ServiceProcessingException;
import com.ayad.integrationservice.common.utils.ProcessorUtility;
import com.ayad.integrationservice.domain.model.dtos.ProcessingResult;
import com.ayad.integrationservice.domain.model.entities.CvProcessorJob;
import com.ayad.integrationservice.domain.model.entities.Status;
import com.ayad.integrationservice.domain.repository.ifc.CVProcessorRepository;
import com.ayad.integrationservice.domain.service.ifc.CVProcessorService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Service
public class CVProcessorServiceImpl implements CVProcessorService {

    private static final String STORAGE_FOLDER = "external-storage";  // Folder inside resources

    private final CVProcessorRepository cvProcessorRepository;
    private final TkExtractionClient tkExtractionClient;

    public CVProcessorServiceImpl(CVProcessorRepository cvProcessorRepository, TkExtractionClient tkExtractionClient) {
        this.cvProcessorRepository = cvProcessorRepository;
        this.tkExtractionClient = tkExtractionClient;
    }

    @Override
    public String submitCV(MultipartFile cv) {
        String processId = UUID.randomUUID().toString();
        log.info("Generated process ID: {}", processId);
        log.debug("Starting to process file: {}", cv.getOriginalFilename());
        String filePath = storeFile(cv, processId);
        log.debug("File stored at path: {}", filePath);
        CvProcessorJob cvProcessorJob = CvProcessorJob.builder().processId(processId)
                .filePath(filePath)
                .status(Status.IN_PROGRESS)
                .build();
        // Save metadata with the file path in the database
        cvProcessorRepository.save(cvProcessorJob);
        log.info("Saved job in repository with CV name: {} and process ID: {}", cv.getOriginalFilename(), processId);
        // Call the TK Extraction Service asynchronously
        processTkExtraction(cvProcessorJob, cv.getOriginalFilename());

        return processId;  // Return process ID immediately
    }

    @Override
    public ProcessingResult getProcessingResult(String processId) {
        // Fetch the CvProcessorJob from the repository and map it to a ProcessingResult
        return cvProcessorRepository.findById(processId)
                .map(cvProcessorJob -> new ProcessingResult(
                        cvProcessorJob.getProcessId(),
                        cvProcessorJob.getStatus().toString(),
                        cvProcessorJob.getResult()))
                .orElseThrow(() -> new ProcessIdNotFoundException(HttpStatus.NOT_FOUND, ProcessorUtility.CONSTRAINT_VIOLATIONS,
                        String.format(ProcessorUtility.NO_Process_MATCHING_FOUND, processId)));
    }


    private String storeFile(MultipartFile file, String processId) {
        try {
            log.debug("Storing file for process ID: {}", processId);
            // Get the path to the resources folder
            ClassPathResource resource = new ClassPathResource("");
            Path storageDirectory = Paths.get(resource.getFile().getAbsolutePath(), STORAGE_FOLDER);

            // Create the directory if it doesn't exist
            if (!Files.exists(storageDirectory)) {
                Files.createDirectories(storageDirectory);
            }

            // Define the file path with the processId as the filename
            Path filePath = Paths.get(storageDirectory.toString(), processId + "_" + file.getOriginalFilename());

            // Write the file to the resources folder
            Files.write(filePath, file.getBytes());

            return filePath.toString();
        } catch (IOException e) {
            log.error("Error saving file to resources folder: {}", file.getOriginalFilename(), e);  // Error logging
            throw new ServiceProcessingException(
                    HttpStatus.INTERNAL_SERVER_ERROR,
                    ProcessorUtility.FILE_STORAGE_ERROR,
                    String.format(ProcessorUtility.STORE_ERROR_MESSAGE, processId, e.getMessage())  // Custom Message
            );
        }
    }

    private void processTkExtraction(CvProcessorJob cvProcessorJob, String cvFileName) {
        CompletableFuture<String> extractionFuture = tkExtractionClient.callTKExtractionService(cvProcessorJob.getFilePath());
        log.info("Initiated TK Extraction Service call for CV name: {} and process ID: {}", cvFileName, cvProcessorJob.getProcessId());

        // Handle the async response
        extractionFuture.whenComplete((result, exception) -> {
            if (exception == null && result != null && !result.isEmpty()) {
                // Success - update the status to COMPLETED and save the result
                log.info("TK Extraction Service call completed successfully. for CV name: {} and Process ID: {}. Result: {}", cvFileName, cvProcessorJob.getProcessId(), result);
                cvProcessorJob.setStatus(Status.COMPLETED);
                cvProcessorJob.setResult(result);
            } else {
                // Log the error
                log.error("Error occurred while processing TK Extraction Service for CV name: {} and Process ID: {}", cvFileName, cvProcessorJob.getProcessId(), exception);
                cvProcessorJob.setStatus(Status.ERROR);  // Error - update the status to ERROR
            }

            // Save the updated job status and result
            cvProcessorRepository.save(cvProcessorJob);
            log.info("Updated status for CV name: {} and Process ID: {}. New status: {}", cvFileName, cvProcessorJob.getProcessId(), cvProcessorJob.getStatus());
        });
    }
}
