package ru.practicum.gateway.exception.modelException;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
