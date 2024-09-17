package ru.practicum.server.exception.modelException;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
