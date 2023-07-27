package com.ayad.stockservice.common.exception;

import com.ayad.stockservice.common.utils.StockUtility;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Slf4j
@ControllerAdvice
public class StockExceptionHandler extends ResponseEntityExceptionHandler {


    @ExceptionHandler(StockNotFoundException.class)
    public ResponseEntity<String> handleException(StockNotFoundException ex) {
        log.error("handleException: Error occurred ", ex);

        String errorMsg = ex.getErrorMessage();
        HttpStatus statusCode = ex.getStatusCode();

        return new ResponseEntity<>(errorMsg, statusCode);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("handleException: Error occurred ", ex);
        HttpStatus statusCode = HttpStatus.INTERNAL_SERVER_ERROR;

        return new ResponseEntity<>(StockUtility.SERVER_ERROR_MESSAGE, statusCode);
    }
}
