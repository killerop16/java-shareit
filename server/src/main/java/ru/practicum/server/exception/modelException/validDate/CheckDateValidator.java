package ru.practicum.server.exception.modelException.validDate;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import ru.practicum.server.booking.dto.CreateBookingRequest;

import java.time.LocalDateTime;

public class CheckDateValidator implements ConstraintValidator<StartBeforeEndDateValid, CreateBookingRequest> {
    @Override
    public void initialize(StartBeforeEndDateValid constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(CreateBookingRequest createBookingRequest, ConstraintValidatorContext constraintValidatorContext) {
        LocalDateTime start = createBookingRequest.getStart();
        LocalDateTime end = createBookingRequest.getEnd();
        LocalDateTime now = LocalDateTime.now();
        if (start == null || end == null) {
            return false;
        }

        return !(start.isAfter(end) || start.equals(end) || start.isBefore(now));
    }
}
