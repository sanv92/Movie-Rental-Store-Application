package com.example.movierentalstoreapplication.exceptions;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class ViolationResponse {

    private Integer status;
    private HttpStatus code;
    private String message;

    private String details;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    private LocalDateTime timestamp;

    private List<Violation> errors;

    private ViolationResponse() {
        this.timestamp = LocalDateTime.now();
        this.errors = new ArrayList<>();
    }

    public ViolationResponse(HttpStatus code) {
        this();
        this.status = code.value();
        this.code = code;
        this.errors = new ArrayList<>();
    }

    public ViolationResponse(HttpStatus code, Throwable ex) {
        this();
        this.status = code.value();
        this.code = code;
        this.message = "Unexpected error";
        this.details = ex.getLocalizedMessage();
        this.errors = new ArrayList<>();
    }

    public ViolationResponse(HttpStatus code, String message, Throwable ex) {
        this();
        this.status = code.value();
        this.code = code;
        this.message = message;
        this.details = ex.getLocalizedMessage();
        this.errors = new ArrayList<>();
    }

    public Integer getStatus() {
        return status;
    }

    public HttpStatus getCode() {
        return code;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getDebugMessage() {
        return details;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public void setCode(HttpStatus code) {
        this.code = code;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDebugMessage(String debugMessage) {
        this.details = debugMessage;
    }

    public List<Violation> getErrors() {
        return errors;
    }

    public void setErrors(List<Violation> errors) {
        this.errors = errors;
    }

    public void addError(Violation error) {
        if (error != null) {
            errors.add(error);
        }
    }

    private void addError(ObjectError objectError) {
        this.addError(new Violation(
                objectError.getObjectName(),
                objectError.getDefaultMessage()
        ));
    }

    public void addValidationError(FieldError error) {
        this.addError(new ValidationField(
                error.getCode(),
                error.getDefaultMessage(),
                error.getField(),
                error.getRejectedValue()
        ));
    }

    public void addErrors(List<ObjectError> errors) {
        errors.forEach(this::addError);
    }

    public void addValidationErrors(List<FieldError> errors) {
        errors.forEach(this::addValidationError);
    }
}