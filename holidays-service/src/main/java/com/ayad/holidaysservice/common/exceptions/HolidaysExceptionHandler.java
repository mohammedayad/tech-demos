package com.ayad.holidaysservice.common.exceptions;

import com.ayad.holidaysservice.common.utils.HolidaysUtility;
import com.ayad.holidaysservice.domain.model.dtos.ProblemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


/**
 * Global exception handler for the application.
 * Translates exceptions into standardized ProblemDto responses.
 */
@Slf4j
@ControllerAdvice
public class HolidaysExceptionHandler extends ResponseEntityExceptionHandler {


    /**
     * Handles custom service processing exceptions.
     *
     * @param ex The caught ServiceProcessingException
     * @return ResponseEntity with ProblemDto body
     */
    @ExceptionHandler(ServiceProcessingException.class)
    public ResponseEntity<Object> handleException(ServiceProcessingException ex) {
        log.error("handleServiceProcessingException: Error occurred ", ex);
        ProblemDto problem = ProblemDto.builder()
                .status(ex.getStatusCode().value())
                .instance(ex.getStatusCode().getReasonPhrase())
                .type(ex.getStatusCode().getReasonPhrase())
                .detail(ex.getErrorMessage())
                .title(HolidaysUtility.SERVICE_PROCESSING_ERROR_TITLE)
                .build();
        return ResponseEntity.status(ex.getStatusCode().value()).body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleException(IllegalArgumentException ex) {
        log.error("handleIllegalArgumentException: Error occurred ", ex);
        ProblemDto problem = ProblemDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .instance(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .detail(ex.getLocalizedMessage())
                .title(HolidaysUtility.ILLEGAL_ARGUMENT_ERROR_TITLE)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(problem);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("handleException: Error occurred ", ex);
        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        String errorMessage = HolidaysUtility.SERVER_ERROR_MESSAGE;
        String title = HolidaysUtility.INTERNAL_SERVER_ERROR_TITLE;
        if (ex.getCause() instanceof ServiceProcessingException serviceProcessingException) {
            httpStatus = serviceProcessingException.getStatusCode();
            errorMessage = serviceProcessingException.getErrorMessage();
            title = HolidaysUtility.SERVICE_PROCESSING_ERROR_TITLE;
        }
        ProblemDto problem = ProblemDto.builder()
                .status(httpStatus.value())
                .instance(httpStatus.getReasonPhrase())
                .type(httpStatus.getReasonPhrase())
                .detail(errorMessage)
                .title(title)
                .build();
        return ResponseEntity.status(httpStatus.value()).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleHandlerMethodValidationException(
            HandlerMethodValidationException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {

        String errorMessage = ex.getAllErrors().get(0).getDefaultMessage();
        log.error("handleValidationException: Error occurred {}", errorMessage, ex);
        ProblemDto problem = ProblemDto.builder()
                .status(ex.getStatusCode().value())
                .instance(ex.getReason())
                .type(ex.getReason())
                .detail(errorMessage)
                .title(HolidaysUtility.ILLEGAL_ARGUMENT_ERROR_TITLE)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(problem);

    }
}
