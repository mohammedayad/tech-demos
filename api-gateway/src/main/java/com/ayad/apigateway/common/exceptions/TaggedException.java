package com.ayad.apigateway.common.exceptions;

import com.ayad.apigateway.common.models.ErrorResponse;
import org.springframework.http.HttpStatus;

public class TaggedException extends RuntimeException {
    private final String tag;
    private final HttpStatus status;
    //if this fiels is set, the exception will be converted to BAD REQUEST
    //when reported to client (to hide sensitive information)
    private boolean generify;

    TaggedException(Throwable cause, HttpStatus status, String tag, String message) {
        super(message, cause);
        this.tag = tag;
        this.status = status;
        this.generify = true;
    }

    TaggedException(HttpStatus status, String tag, String message) {
        super(message);
        this.tag = tag;
        this.status = status;
        this.generify = true;
    }

    TaggedException(HttpStatus status, String tag, String message, boolean generify) {
        this(status, tag, message);
        this.generify = generify;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getTag() {
        return tag;
    }

    public boolean isGenerify() {
        return generify;
    }

    public ErrorResponse toResponse() {
        return new ErrorResponse(getTag(), getMessage());
    }
}

