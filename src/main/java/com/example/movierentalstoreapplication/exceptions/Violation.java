package com.example.movierentalstoreapplication.exceptions;

import java.util.Objects;

class Violation {
    private String code;
    private String message;

    public Violation(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Violation)) return false;
        Violation error = (Violation) o;
        return Objects.equals(code, error.code) &&
                Objects.equals(message, error.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message);
    }

    @Override
    public String toString() {
        return "Violation{" +
                "code='" + code + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}