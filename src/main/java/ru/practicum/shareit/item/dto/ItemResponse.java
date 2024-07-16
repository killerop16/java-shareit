package ru.practicum.shareit.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.item.comment.dto.CommentResponse;

import java.util.ArrayList;
import java.util.List;


@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemResponse {
    private int id;
    private  int idOwner;
    private  String name;
    private String description;
    private Boolean available;

    private BookingResponse lastBooking;
    private BookingResponse nextBooking;

    private List<CommentResponse> comments = new ArrayList<>();
}
