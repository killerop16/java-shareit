package ru.practicum.shareit.booking.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.item.dto.ItemResponseDepends;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.dto.UserResponseDepends;
import ru.practicum.shareit.user.model.User;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
