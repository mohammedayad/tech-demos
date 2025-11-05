package com.ahold.technl.sandbox.common.exceptions;


import com.ahold.technl.sandbox.domain.model.dtos.ProblemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static com.ahold.technl.sandbox.common.utils.DeliveriesUtility.*;

/**
 * Global exception handler for the application.
 * Translates exceptions into standardized ProblemDto responses.
 */

@Slf4j
@ControllerAdvice
public class DeliveriesExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleException(IllegalArgumentException ex) {
        log.error("handleIllegalArgumentException: Error occurred ", ex);
        ProblemDto problem = ProblemDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .instance(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .detail(ex.getLocalizedMessage())
                .title(ILLEGAL_ARGUMENT_ERROR_TITLE)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(problem);
    }


    /**
     * Handles custom service processing exceptions.
     *
     * @param ex The caught ServiceProcessingException
     * @return ResponseEntity with ProblemDto body
     */
    @ExceptionHandler(DeliveryProcessingException.class)
    public ResponseEntity<Object> handleException(DeliveryProcessingException ex) {
        log.error("handleDeliveryProcessingException: Error occurred ", ex);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDto problem = ProblemDto.builder()
                .status(httpStatus.value())
                .instance(httpStatus.getReasonPhrase())
                .type(httpStatus.getReasonPhrase())
                .detail(ex.getLocalizedMessage())
                .title(SERVICE_PROCESSING_ERROR_TITLE)
                .build();
        return ResponseEntity.status(httpStatus.value()).body(problem);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("handleException: Error occurred ", ex);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        ProblemDto problem = ProblemDto.builder()
                .status(httpStatus.value())
                .instance(httpStatus.getReasonPhrase())
                .type(httpStatus.getReasonPhrase())
                .detail(SERVER_ERROR_MESSAGE)
                .title(INTERNAL_SERVER_ERROR_TITLE)
                .build();
        return ResponseEntity.status(httpStatus.value()).body(problem);
    }
}
