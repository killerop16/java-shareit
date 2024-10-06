package ru.practicum.server.booking.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.exception.modelException.validDate.StartBeforeEndDateValid;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@StartBeforeEndDateValid
public class CreateBookingRequest {
    private LocalDateTime start;
    private LocalDateTime end;
    @NotNull
    private Integer itemId;
}
