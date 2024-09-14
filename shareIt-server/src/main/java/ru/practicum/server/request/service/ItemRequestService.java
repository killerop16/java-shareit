package ru.practicum.server.request.service;

import ru.practicum.server.request.dto.CreateItemRequest;
import ru.practicum.server.request.dto.ItemRequestResponse;
import ru.practicum.server.request.dto.ItemRequestResponseWithItems;

import java.util.List;

public interface ItemRequestService {
    ItemRequestResponse create(CreateItemRequest itemRequestDto, int userId);

    List<ItemRequestResponseWithItems> foundRequestsById(int id);

    List<ItemRequestResponse> findAll();

    ItemRequestResponseWithItems foundById(int requestId, int userId);
}
