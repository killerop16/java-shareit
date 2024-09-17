package ru.practicum.gateway.request.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.gateway.item.dto.RequestItemResponse;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestResponseWithItems {
    private int id;
    private String description;
    private LocalDateTime created;
    private List<RequestItemResponse> items;
}
