package com.ayad.integrationservice.common.exception;


import com.ayad.integrationservice.common.utils.ProcessorUtility;
import com.ayad.integrationservice.domain.model.dtos.ProblemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class ProcessorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ProcessIdNotFoundException.class)
    public ResponseEntity<Object> handleException(ProcessIdNotFoundException ex) {
        log.error("handleException: Error occurred ", ex);
        ProblemDto problem = ProblemDto.builder()
                .status(ex.getStatusCode().value())
                .instance(ex.getStatusCode().getReasonPhrase())
                .type(ex.getStatusCode().getReasonPhrase())
                .detail(ex.getErrorMessage())
                .title(ProcessorUtility.PROCESS_ID_NOT_FOUND_ERROR_TITLE)
                .build();
        return ResponseEntity.status(ex.getStatusCode().value()).body(problem);
    }

    @ExceptionHandler(ServiceProcessingException.class)
    public ResponseEntity<Object> handleException(ServiceProcessingException ex) {
        log.error("handleException: Error occurred ", ex);
        ProblemDto problem = ProblemDto.builder()
                .status(ex.getStatusCode().value())
                .instance(ex.getStatusCode().getReasonPhrase())
                .type(ex.getStatusCode().getReasonPhrase())
                .detail(ex.getErrorMessage())
                .title(ProcessorUtility.INTERNAL_SERVER_ERROR_TITLE)
                .build();
        return ResponseEntity.status(ex.getStatusCode().value()).body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleException(IllegalArgumentException ex) {
        log.error("handleException: Error occurred ", ex);
        ProblemDto problem = ProblemDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .instance(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .detail(ex.getLocalizedMessage())
                .title(ProcessorUtility.ILLEGAL_ARGUMENT_ERROR_TITLE)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("handleException: Error occurred ", ex);
        ProblemDto problem = ProblemDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .instance(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .type(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .detail(ProcessorUtility.SERVER_ERROR_MESSAGE)
                .title(ProcessorUtility.INTERNAL_SERVER_ERROR_TITLE)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(problem);
    }
}
