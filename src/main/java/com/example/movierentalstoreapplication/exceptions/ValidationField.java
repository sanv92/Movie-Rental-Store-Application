package com.example.movierentalstoreapplication.exceptions;

import java.util.Objects;

final class ValidationField extends Violation {
    private String field;
    private Object rejectedValue;

    public ValidationField(String code, String message, String field, Object rejectedValue) {
        super(code, message);

        this.field = field;
        this.rejectedValue = rejectedValue;
    }

    public String getField() {
        return field;
    }

    public Object getRejectedValue() {
        return rejectedValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ValidationField)) return false;
        if (!super.equals(o)) return false;
        ValidationField that = (ValidationField) o;
        return Objects.equals(field, that.field) &&
                Objects.equals(rejectedValue, that.rejectedValue);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), field, rejectedValue);
    }

    @Override
    public String toString() {
        return "ValidationField{" +
                "field='" + field + '\'' +
                ", rejectedValue=" + rejectedValue +
                '}';
    }
}