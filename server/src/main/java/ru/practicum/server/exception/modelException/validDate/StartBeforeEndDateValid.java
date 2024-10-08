package ru.practicum.server.exception.modelException.validDate;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE_USE)
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CheckDateValidator.class)
@Documented
public @interface StartBeforeEndDateValid {
    String message() default "Start must be before end or not null";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
