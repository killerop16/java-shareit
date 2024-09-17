package ru.practicum.gateway.exception.controller;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import ru.practicum.gateway.exception.modelException.DuplicateEmailException;
import ru.practicum.gateway.exception.modelException.NotFoundException;
import ru.practicum.gateway.exception.modelException.ValidationException;




@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler({ValidationException.class, MethodArgumentNotValidException.class})
    public ResponseEntity<ErrorResponse> handleValidation(Exception exception) {
        log.debug("Получен статус 400 BAD_REQUEST {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleForbidden(EntityNotFoundException exception) {
        log.debug("Получен статус 403 FORBIDDEN {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleNotFound(NotFoundException exception) {
        log.debug("Получен статус 404 NOT_FOUND {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleDuplicateEmail(DuplicateEmailException exception) {
        log.debug("Получен статус 409 CONFLICT {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponse(exception.getMessage()));
    }

    @ExceptionHandler(HttpClientErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpClientError(HttpClientErrorException exception) {
        HttpStatus statusCode = (HttpStatus) exception.getStatusCode();

        log.debug("Получен статус {} от внешнего сервиса: {}", statusCode.value(), exception.getMessage(), exception);

        return ResponseEntity.status(statusCode).body(new ErrorResponse("Ошибка при взаимодействии с внешним сервисом: " + exception.getMessage()));
    }

    @ExceptionHandler(HttpServerErrorException.class)
    public ResponseEntity<ErrorResponse> handleHttpServerError(HttpServerErrorException exception) {
        log.debug("Получен статус 500 INTERNAL_SERVER_ERROR от внешнего сервиса {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Ошибка на стороне сервера при взаимодействии с внешним сервисом: " + exception.getMessage()));
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleGenericError(Throwable exception) {
        log.debug("Получен статус 500 INTERNAL_SERVER_ERROR {}", exception.getMessage(), exception);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse("Внутренняя ошибка сервера: " + exception.getMessage()));
    }
}