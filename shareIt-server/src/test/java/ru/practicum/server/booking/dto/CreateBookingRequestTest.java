package ru.practicum.server.booking.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.ConstraintViolation;
import java.time.LocalDateTime;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateBookingRequestTest {

    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // Для работы с LocalDateTime
        // Устанавливаем формат для LocalDateTime
        objectMapper.findAndRegisterModules();
        objectMapper.setDateFormat(new java.text.SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"));

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCreateBookingRequestSerialization() throws Exception {
        CreateBookingRequest createBookingRequest = new CreateBookingRequest(
                LocalDateTime.of(2024, 9, 14, 10, 0),
                LocalDateTime.of(2024, 9, 15, 10, 0),
                1
        );

        // Сериализация объекта в строку JSON
        String jsonString = objectMapper.writeValueAsString(createBookingRequest);

        // Проверяем содержимое JSON
        assertThat(jsonString).contains("\"start\":\"2024-09-14T10:00:00\"");
        assertThat(jsonString).contains("\"end\":\"2024-09-15T10:00:00\"");
        assertThat(jsonString).contains("\"itemId\":1");
    }

    @Test
    public void testCreateBookingRequestValidDates() {
        CreateBookingRequest request = new CreateBookingRequest(
                LocalDateTime.now().plusDays(1), // start через 1 день
                LocalDateTime.now().plusDays(2), // end через 2 дня
                1 // itemId
        );

        Set<ConstraintViolation<CreateBookingRequest>> violations = validator.validate(request);

        assertThat(violations).isEmpty(); // Ожидаем, что валидация пройдет успешно
    }

    @Test
    public void testCreateBookingRequestInvalidDates() {
        CreateBookingRequest request = new CreateBookingRequest(
                LocalDateTime.now().plusDays(2), // start через 2 дня
                LocalDateTime.now().plusDays(1), // end через 1 день
                1 // itemId
        );

        Set<ConstraintViolation<CreateBookingRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("Start must be before end or not null");
    }

    @Test
    public void testCreateBookingRequestMissingItemId() {
        CreateBookingRequest request = new CreateBookingRequest(
                LocalDateTime.now().plusDays(1), // start через 1 день
                LocalDateTime.now().plusDays(2), // end через 2 дня
                null // itemId
        );

        Set<ConstraintViolation<CreateBookingRequest>> violations = validator.validate(request);

        assertThat(violations).isNotEmpty();
        assertThat(violations.iterator().next().getMessage()).isEqualTo("не должно равняться null");
    }
}
