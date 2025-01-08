package com.ayad.apigateway.common.exceptions;

import lombok.experimental.UtilityClass;
import org.springframework.http.HttpStatus;

import java.text.MessageFormat;

@UtilityClass
public class Exceptions {

    private static final String MISSING_HEADER = "Header {0} is required";
    private static final String WRONG_HEADER_FORMAT = "Wrong format of header {0}";

    //TODO: review this error message "Wrong length {0} of extracted value in %s "
    private static final String WRONG_HEADER_SIZE = "Wrong size of header {0}, expected {1} bytes in {2}";

    private static final String MISSING_SALT_VERIFIER = "Missing salt or verifier for device ''{0}''";
    private static final String UNSECURED_HEADERS = "Detected unsecured header transmission for headers: {0}";

    public static TaggedException missingHeaderException(String header) {
        return badRequestException(MISSING_HEADER, header);
    }

    public static TaggedException wrongHeaderFormatException(String header) {
        return badRequestException(WRONG_HEADER_FORMAT, header);
    }

    public static TaggedException wrongHeaderSizeException(String header, int size, String value) {
        return badRequestException(WRONG_HEADER_SIZE, header, size, value);
    }

    public static TaggedException badRequestException(String format, Object... args) {
        return new BadRequestException(MessageFormat.format(format, args));
    }

    public static TaggedException invalidKeyException(Throwable cause) {
        return new TaggedException(cause, HttpStatus.FORBIDDEN, "R021", "Invalid data or key");
    }

    public static TaggedException missingSaltVerifierException(String hardwareId) {
        return new TaggedException(HttpStatus.FORBIDDEN, "MISSING_SALT_VERIFIER", MessageFormat.format
                (MISSING_SALT_VERIFIER, hardwareId));
    }

    public static TechnicalError technicalError(Exception e) {
        return new TechnicalError(e);
    }

    public static TaggedException unsecuredHeadersExceptions(String headers) {
        return badRequestException(UNSECURED_HEADERS, headers);
    }

    public static TaggedException pageNotFound(String requestURI) {
        return new TaggedException(HttpStatus.NOT_FOUND, "PAGE NOT FOUND", requestURI);
    }

    public static TaggedException malformedTokenException() {
        return badRequestException("Malformed token");
    }

    public static TaggedException inconsistentUserIdException() {
        return forbidden("USER_MISMATCH", "Registered user id does not match current user or token");
    }

    public static TaggedException forbidden(String tag, String message) {
        return new TaggedException(HttpStatus.FORBIDDEN, tag, message, false);
    }

    public static TaggedException tooManyRequests() {
        return new TaggedException(HttpStatus.TOO_MANY_REQUESTS, "TOO_MANY_REQUESTS", "Too many requests", false);
    }

    public static TaggedException unsupportedClient() {
        return new TaggedException(HttpStatus.FORBIDDEN, "AFTER_DECOMMISSION_DEADLINE", "App has been decommissioned and account deleted", false);
    }

    public static TaggedException unsupportedVersion() {
        return new TaggedException(HttpStatus.PRECONDITION_FAILED, "R1500", "Client version not supported", false);
    }

}

