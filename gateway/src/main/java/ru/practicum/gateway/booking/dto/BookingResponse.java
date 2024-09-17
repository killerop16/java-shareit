package ru.practicum.gateway.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.gateway.item.dto.ItemResponseDepends;
import ru.practicum.gateway.user.dto.UserResponseDepends;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponse {
    private int id;
    private LocalDateTime start;
    private LocalDateTime end;
    private Status status;

    private ItemResponseDepends item;
    private UserResponseDepends booker;
}
