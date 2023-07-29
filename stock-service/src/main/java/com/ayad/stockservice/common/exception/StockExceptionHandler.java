package com.ayad.stockservice.common.exception;

import com.ayad.stockservice.common.utils.StockUtility;
import com.ayad.stockservice.domain.model.dtos.ProblemDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@ControllerAdvice
public class StockExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<Object> handleException(StockNotFoundException ex) {
        log.error("handleException: Error occurred ", ex);
//        String errorMsg = ex.getErrorMessage();
//        HttpStatus statusCode = ex.getStatusCode();
//        return new ResponseEntity<>(errorMsg, statusCode);
        ProblemDto problem = ProblemDto.builder()
                .status(ex.getStatusCode().value())
                .instance(ex.getStatusCode().getReasonPhrase())
                .type(ex.getStatusCode().getReasonPhrase())
                .detail(ex.getErrorMessage())
                .title(StockUtility.STOCK_NOT_FOUND_ERROR_TITLE)
                .build();
        return ResponseEntity.status(ex.getStatusCode().value()).body(problem);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Object> handleException(IllegalArgumentException ex) {
        log.error("handleException: Error occurred ", ex);
        // return new ResponseEntity<>(ex.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        ProblemDto problem = ProblemDto.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .instance(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .type(HttpStatus.BAD_REQUEST.getReasonPhrase())
                .detail(ex.getLocalizedMessage())
                .title(StockUtility.ILLEGAL_ARGUMENT_ERROR_TITLE)
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST.value()).body(problem);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.error("handleException: Error occurred ", ex);
        String errorMessage = ex.getBindingResult().getFieldErrors().get(0).getDefaultMessage();
        //List<String> validationList = ex.getBindingResult().getFieldErrors().stream().map(fieldError->fieldError.getDefaultMessage()).collect(Collectors.toList());
        //log.info("Validation error list : "+validationList);
        // return new ResponseEntity<>(errorMessage, status);
        ProblemDto problem = ProblemDto.builder()
                .status(status.value())
                .instance(status.getReasonPhrase())
                .type(status.getReasonPhrase())
                .detail(errorMessage)
                .title(StockUtility.ILLEGAL_ARGUMENT_ERROR_TITLE)
                .build();
        return ResponseEntity.status(status.value()).body(problem);

    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        log.error("handleException: Error occurred ", ex);
        //return new ResponseEntity<>(StockUtility.SERVER_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
        ProblemDto problem = ProblemDto.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .instance(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .type(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
                .detail(StockUtility.SERVER_ERROR_MESSAGE)
                .title(StockUtility.INTERNAL_SERVER_ERROR_TITLE)
                .build();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(problem);
    }

//    @ResponseStatus(HttpStatus.BAD_REQUEST)
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public Map<String, String> handleValidationExceptions(
//            MethodArgumentNotValidException ex) {
//        Map<String, String> errors = new HashMap<>();
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            String errorMessage = error.getDefaultMessage();
//            errors.put(fieldName, errorMessage);
//        });
//        return errors;
//
//    }
}
