package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingResponseDepends;
import ru.practicum.shareit.item.comment.dto.CommentResponse;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private int id;
    private  int idOwner;
    private  String name;
    private String description;
    private Boolean available;

    private BookingResponseDepends lastBooking;
    private BookingResponseDepends nextBooking;

    private List<CommentResponse> comments = new ArrayList<>();
}
