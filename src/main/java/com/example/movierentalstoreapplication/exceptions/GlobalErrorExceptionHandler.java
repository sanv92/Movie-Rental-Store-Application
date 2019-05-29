package com.example.movierentalstoreapplication.exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class GlobalErrorExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(ResourceNotFoundException ex) {
        ViolationResponse errorResponse = new ViolationResponse(HttpStatus.NOT_FOUND);
        errorResponse.setMessage(ex.getMessage());

        return buildResponseEntity(errorResponse);
    }

    @ExceptionHandler(javax.persistence.EntityNotFoundException.class)
    protected ResponseEntity<Object> handleEntityNotFound(javax.persistence.EntityNotFoundException ex) {
        return buildResponseEntity(new ViolationResponse(HttpStatus.NOT_FOUND, ex));
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatus status,
            WebRequest request
    ) {
        ViolationResponse errorResponse = new ViolationResponse(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage("Validation error");
        errorResponse.addValidationErrors(ex.getBindingResult().getFieldErrors());
        errorResponse.addErrors(ex.getBindingResult().getGlobalErrors());
        errorResponse.setDebugMessage(ex.getMessage());

        return buildResponseEntity(errorResponse);
    }

    @Override
    protected ResponseEntity<Object> handleNoHandlerFoundException(
            NoHandlerFoundException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ViolationResponse errorResponse = new ViolationResponse(HttpStatus.BAD_REQUEST);
        errorResponse.setMessage(String.format("Could not find the %s method for URL %s", ex.getHttpMethod(), ex.getRequestURL()));
        errorResponse.setDebugMessage(ex.getMessage());

        return buildResponseEntity(errorResponse);
    }

    private ResponseEntity<Object> buildResponseEntity(ViolationResponse errorResponse) {
        logger.error(errorResponse);

        return ResponseEntity
                .status(errorResponse.getStatus())
                .body(errorResponse);
    }
}