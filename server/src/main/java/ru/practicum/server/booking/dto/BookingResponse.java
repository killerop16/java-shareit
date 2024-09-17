package ru.practicum.server.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.item.dto.ItemResponseDepends;
import ru.practicum.server.user.dto.UserResponseDepends;

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
