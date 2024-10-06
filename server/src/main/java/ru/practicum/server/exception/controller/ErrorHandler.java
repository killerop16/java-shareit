package ru.practicum.server.exception.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.server.exception.modelException.DuplicateEmailException;
import ru.practicum.server.exception.modelException.NotFoundException;
import ru.practicum.server.exception.modelException.ValidationException;


@Slf4j
@RestControllerAdvice(basePackages = "ru.practicum.server")
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidation(Exception exception) {
        log.debug("Получен статус 400 BAD_REQUEST {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleNotFound(EntityNotFoundException exception) {
        log.debug("Получен статус 403 FORBIDDEN {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(NotFoundException exception) {
        log.debug("Получен статус 404 NOT_FOUND {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicateEmail(DuplicateEmailException exception) {
        log.debug("Получен статус 409 CONFLICT {}", exception.getMessage(), exception);
        return new ErrorResponse(exception.getMessage());
    }
}