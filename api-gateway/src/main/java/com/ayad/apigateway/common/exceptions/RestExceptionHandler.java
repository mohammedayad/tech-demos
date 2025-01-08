package com.ayad.apigateway.common.exceptions;

import com.ayad.apigateway.common.models.ErrorResponse;
import com.ayad.apigateway.filters.ResponseEncryptionGatewayFilterFactory;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.rewrite.ModifyResponseBodyGatewayFilterFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebExceptionHandler;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
@Slf4j
public class RestExceptionHandler implements WebExceptionHandler {

    private final ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilterFactory;
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    public RestExceptionHandler(ModifyResponseBodyGatewayFilterFactory modifyResponseBodyFilterFactory) {
        this.modifyResponseBodyFilterFactory = modifyResponseBodyFilterFactory;
    }


    @Override
    public @NotNull Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
        log.error("RestExceptionHandler: Caught exception - {}", ex.getMessage(), ex);

//        // Check if the response is already committed
//        if (exchange.getResponse().isCommitted()) {
//            log.warn("Response is already committed, skipping further handling.");
//            return Mono.empty(); // This ensures a Mono<Void> is returned
//        }
//
//        // Set the response status without committing
//        exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
//
//        // Return an empty Mono to indicate exception has been handled
////        return Mono.empty()
////                .doOnSuccess(aVoid -> log.info("RestExceptionHandler: Mono<Void> completed without committing response."))
////                .doOnError(err -> log.error("RestExceptionHandler: Error in Mono<Void> - {}", err.getMessage()))
////                .then();
//        return Mono.error(ex);

        log.error("Handling exception: {}", ex.getMessage());
        return Mono.defer(() -> {
            // Example: Creating a fallback response
            exchange.getResponse().setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
            return exchange.getResponse().setComplete(); // Mark response as complete
        }).then(invokeCustomFilter(exchange)); // Propagate empty signal to allow other filters

//        return Mono.defer(() -> {
//            // Custom logic after exception handling, such as logging or modifying headers
//            return invokeCustomFilter(exchange);  // Manually invoke the filter
//        }).onErrorResume(e -> {
//            // Handle any errors that happen during filter invocation
//            log.error("Error during filter invocation", e);
//            return Mono.empty();  // Return Mono.empty() if error occurs
//        });

    }

    private Mono<Void> invokeCustomFilter(ServerWebExchange exchange) {
        // Assuming ModifyResponseBodyGatewayFilterFactory is already injected or instantiated

        // Create an instance of ResponseEncryptionGatewayFilterFactory
        ResponseEncryptionGatewayFilterFactory responseEncryptionFilterFactory =
                new ResponseEncryptionGatewayFilterFactory(modifyResponseBodyFilterFactory);

        // Configure the filter (in this case, we are passing a default config)
        ResponseEncryptionGatewayFilterFactory.Config config = new ResponseEncryptionGatewayFilterFactory.Config();

        // Apply the filter factory to get the GatewayFilter
        GatewayFilter encryptionFilter = responseEncryptionFilterFactory.apply(config);
        // Create a mock GatewayFilterChain (simplified for this case)
        GatewayFilterChain mockChain = new GatewayFilterChain() {
            @Override
            public Mono<Void> filter(ServerWebExchange exchange) {
                // Continue with an empty chain, as we're focusing on invoking the filter logic
                return Mono.empty();
            }
        };

        // Execute the custom filter logic
        return encryptionFilter.filter(exchange, mockChain);
    }


//    @Override
//    public @NotNull Mono<Void> handle(@NotNull ServerWebExchange exchange, @NotNull Throwable ex) {
//        // use try-catch mechanism to identify exception type
//        try {
//            // Set an attribute to indicate an exception was handled
////            exchange.getAttributes().put("custom.error.handled", true);
//            throw ex;
//        } catch (TaggedException e) {
//            return handleTaggedException(exchange, e);
////        } catch (TechnicalError e) {
////            return handleTechnicalError(exchange, e);
////        } catch (HttpClientErrorException | HttpServerErrorException e) {
////            return handleRestTemplateClientException(exchange, e);
////        } catch (SocketTimeoutException e) {
////            return handleSocketTimeout(exchange, e);
////        } catch (UnsupportedMediaTypeStatusException e) {
////            return handleHttpMediaTypeNotSupported(exchange, e);
////        } catch (MethodArgumentNotValidException e) {
////            return handleMethodArgumentNotValid(exchange, e);
////        } catch (DynamicValidationException e) {
////            return handleDynamicValidationException(exchange, e);
////        } catch (HttpMessageNotReadableException e) {
////            return handleHttpMessageNotReadable(exchange, e);
////        } catch (ErrorResponseException e) {
////            return handleExceptionInternal(exchange, e);
//        } catch (Throwable e) {
////            return handleAnyThrowable(exchange, e);
//            return null;
//        }
//    }

    @SneakyThrows
    private Mono<Void> addErrorToResponse(ServerHttpResponse response, Object errorResponse, HttpStatus status,ServerWebExchange exchange) {
        return addErrorToResponse(response, errorResponse, status, new HttpHeaders(),exchange);
    }

    @SneakyThrows
    private Mono<Void> addErrorToResponse(ServerHttpResponse response, Object errorResponse, HttpStatus status, HttpHeaders responseHeaders,ServerWebExchange exchange) {
        response.setStatusCode(status);

        String body = OBJECT_MAPPER.writer().writeValueAsString(errorResponse);
        DataBuffer responseBuffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        // TODO SB check if it is work fine
        response.getHeaders().putAll(responseHeaders);
        return response.writeWith(Mono.just(responseBuffer));
    }

//    protected @NotNull Mono<Void> handleTechnicalError(@NotNull ServerWebExchange exchange, TechnicalError ex) {
//        log.error("Handling technical error", ex);
//        Throwable cause = ex.getCause();
//        ErrorResponse response = new ErrorResponse(ex.getMessage(), cause != null ? cause.getMessage() : "");
//        return addErrorToResponse(exchange.getResponse(), response, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

    /**
     * Handle errors 4xx and 5xx sent by back-end via integration with rest client.
     */
//    protected @NotNull Mono<Void> handleRestTemplateClientException(@NotNull ServerWebExchange exchange, HttpStatusCodeException ex) {
//        if (ex.getStatusCode().is4xxClientError()) {
//            log.info("Handling rest template client exception {}, message {}", ex.getClass().getSimpleName(), ex.getMessage());
//        } else {
//            log.error("Handling rest template server exception", ex);
//        }
//
//        Map<String, Object> response = new HashMap<>(2);
//        response.put("code", messageFromStatusCode(ex.getStatusCode()));
//        response.put("message", "");
//
//        // Try to parse a more meaningful error from the response.
//        try {
//            response = OBJECT_MAPPER.readerFor(Map.class).readValue(ex.getResponseBodyAsString());
//        } catch (IOException e) {
//            log.warn("Handling exception when trying to parse the body of the error response from upstream: \"{}\"", ex
//                    .getResponseBodyAsString());
//        }
//
//        HttpHeaders responseHeaders = new HttpHeaders();
//        if (ex.getResponseHeaders() != null) {
//            // don't propagate transfer encoding because it might change
//            responseHeaders.putAll(ex.getResponseHeaders());
//            responseHeaders.remove(HttpHeaders.TRANSFER_ENCODING);
//        }
//
//        // Propagate headers from error, they might contain Attempts-Left, Retry-After, etc. used by mobile app.
//        return addErrorToResponse(exchange.getResponse(), response, httpCodeToStatus(ex.getStatusCode()), responseHeaders);
//    }

    @NotNull
    private HttpStatus httpCodeToStatus(HttpStatusCode code) {
        return HttpStatus.valueOf(code.value());
    }

    /**
     * Thrown by the application
     */
    private Mono<Void> handleTaggedException(ServerWebExchange exchange, TaggedException ex) {
        log.info("Handling tagged exception", ex);
        ErrorResponse errorResponse = ex.toResponse();
        HttpStatus status = ex.getStatus();

        if (ex.isGenerify()) {
            BadRequestException badRequestException = new BadRequestException(ex.getMessage());
            errorResponse = badRequestException.toResponse();
            status = badRequestException.getStatus();
        }
//        return addErrorToResponse(exchange.getResponse(), errorResponse, status)
//                .then(Mono.defer(Mono::empty)); // Signal chain continuation


//        Mono<Void> completion = addErrorToResponse(exchange.getResponse(), errorResponse, status,exchange)
//                 .then(Mono.defer(Mono::empty));
//        completion = completion.doOnError(error -> exchange.getAttributes().put(HANDLED_WEB_EXCEPTION, error))
//                    .onErrorResume(e -> this.handle(exchange, e));
//        return completion;

        // Add error to response and signal continuation
//        Mono<Void>  completion = addErrorToResponse(exchange.getResponse(), errorResponse, status, exchange)
//                .onErrorResume(error -> {
//                    // Log the error and continue gracefully
//                    exchange.getAttributes().put(HANDLED_WEB_EXCEPTION, error);
//                    log.error("Error while handling exception: {}", error.getMessage(), error);
//                    return Mono.empty(); // Continue the chain after handling the error
//                });
//        return completion;

        return addErrorToResponse(exchange.getResponse(), errorResponse, status,exchange);
    }





//    public Mono<Void> handle(ServerWebExchange exchange) {
//        Mono<Void> completion;
//        try {
//            completion = super.handle(exchange);
//        }
//        catch (Throwable ex) {
//            completion = Mono.error(ex);
//        }
//
//        for (WebExceptionHandler handler : this.exceptionHandlers) {
//            completion = completion.doOnError(error -> exchange.getAttributes().put(HANDLED_WEB_EXCEPTION, error))
//                    .onErrorResume(ex -> handler.handle(exchange, ex));
//        }
//        return completion;
//    }

    /**
     * Uncaught exceptions
     */
//    private Mono<Void> handleAnyThrowable(ServerWebExchange exchange, Throwable ex) {
//        log.error("Handling generic exception", ex);
//        if (ex.getCause() instanceof SocketTimeoutException) {
//            return handleConnectTimeout(exchange);
//        }
//
//        ErrorResponse errorResponse = new ErrorResponse(TechnicalError.TECHNICAL_ERROR_MESSAGE,
//                                                   "Internal Server Error");
//        return addErrorToResponse(exchange.getResponse(), errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//    }

//    private Mono<Void> handleSocketTimeout(ServerWebExchange exchange, SocketTimeoutException ex) {
//        log.error("Handling socket time out", ex);
//        return handleConnectTimeout(exchange);
//    }
//
//    private Mono<Void> handleConnectTimeout(ServerWebExchange exchange) {
//        ErrorResponse errorResponse = new ErrorResponse(TechnicalError.TECHNICAL_ERROR_MESSAGE,
//                                                   "Internal Server Error");
//        return addErrorToResponse(exchange.getResponse(), errorResponse, HttpStatus.GATEWAY_TIMEOUT);
//    }


    /**
     * Handle HttpMediaTypeNotSupportedException. This one triggers when JSON is invalid as well.
     */
//    protected Mono<Void> handleHttpMediaTypeNotSupported(ServerWebExchange exchange,
//                                                          @NotNull UnsupportedMediaTypeStatusException ex) {
//
//        log.info("Handling unsupported media type", ex);
//
//        StringJoiner joiner = new StringJoiner(",");
//        ex.getSupportedMediaTypes().forEach(m -> joiner.add(m.toString()));
//        String message = MessageFormat.format("{0} media type is not supported. Supported media types are: {1}",
//                                              ex.getContentType(), joiner);
//        ErrorResponse response = new BadRequestException(message).toResponse();
//        return addErrorToResponse(exchange.getResponse(), response, httpCodeToStatus(ex.getStatusCode()));
//    }

    /**
     * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid validation.
     */
//    protected @NonNull
//    Mono<Void>  handleMethodArgumentNotValid(ServerWebExchange exchange,
//                                             @NonNull MethodArgumentNotValidException ex) {
//
//        log.info("Handling method argument not valid", ex);
//
//        ErrorResponse response = doHandleMethodArgumentNotValid(ex.getBindingResult());
//        return addErrorToResponse(exchange.getResponse(), response, httpCodeToStatus(ex.getStatusCode()));
//    }

    /**
     * Dynamic validation exception thrown by application
     */
//    protected Mono<Void> handleDynamicValidationException(ServerWebExchange exchange, DynamicValidationException ex) {
//
//        log.info("Handling dynamic validation exception", ex);
//
//        ErrorResponse response = doHandleMethodArgumentNotValid(ex.getBindingResult());
//        return addErrorToResponse(exchange.getResponse(), response, HttpStatus.BAD_REQUEST);
//    }
//
//    private ErrorResponse doHandleMethodArgumentNotValid(BindingResult bindingResult) {
//        String fieldErrors = addValidationErrors(bindingResult.getFieldErrors());
//        String message = "Validation error: " + fieldErrors;
//        return new BadRequestException(message).toResponse();
//    }

    /**
     * Handle HttpMessageNotReadableException. Triggered when cannot deserialize JSON content.
     */
//    protected Mono<Void> handleHttpMessageNotReadable(ServerWebExchange exchange, @NonNull HttpMessageNotReadableException ex) {
//
//        log.info("Handling message not readable {}", ex.getMessage());
//        ErrorResponse response = new BadRequestException("Malformed JSON request").toResponse();
//        return addErrorToResponse(exchange.getResponse(), response, HttpStatus.BAD_REQUEST);
//    }

//    @NonNull
//    protected Mono<Void> handleExceptionInternal(ServerWebExchange exchange, @NonNull ErrorResponseException ex) {
//
//        log.error("Handling exception internal", ex);
//        ErrorResponse response = new ErrorResponse(messageFromStatusCode(ex.getStatusCode()), ex.getMessage());
//        return addErrorToResponse(exchange.getResponse(), response, httpCodeToStatus(ex.getStatusCode()));
//    }

    private String addValidationErrors(List<FieldError> fieldErrors) {
        return fieldErrors.stream().map(this::addValidationError).collect(Collectors.joining(", "));
    }

    private String addValidationError(FieldError fieldError) {
        return MessageFormat.format("field ''{0}'' {1}", fieldError.getField(), fieldError.getDefaultMessage());
    }

    private String messageFromStatusCode(HttpStatusCode status) {
        return status.is4xxClientError() ? BadRequestException.BAD_REQUEST_ERROR_MESSAGE :
                TechnicalError.TECHNICAL_ERROR_MESSAGE;
    }
}
