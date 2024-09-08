package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItems;
import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponse create(CreateItemRequest itemRequestDto, int userId);

    List<ItemRequestResponseWithItems> foundRequestsById(int id);

    List<ItemRequestResponse> findAll();

    ItemRequestResponseWithItems foundById(int requestId, int userId);
}
