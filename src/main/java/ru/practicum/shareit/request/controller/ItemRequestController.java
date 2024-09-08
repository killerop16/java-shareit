package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.request.dto.CreateItemRequest;
import ru.practicum.shareit.request.dto.ItemRequestResponse;
import ru.practicum.shareit.request.dto.ItemRequestResponseWithItems;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.util.HttpHeaders;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestService requestService;

    @PostMapping
    public ItemRequestResponse create(@Valid @RequestBody CreateItemRequest createRequest,
                                      @RequestHeader(HttpHeaders.USER_ID) int userId) {
        return requestService.create(createRequest, userId);
    }

    @GetMapping
    public List<ItemRequestResponseWithItems> foundRequestsById(@RequestHeader(HttpHeaders.USER_ID) int userId) {
        return requestService.foundRequestsById(userId);
    }

    @GetMapping("/all")
    public List<ItemRequestResponse> findAllRequests() {
        return requestService.findAll();
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseWithItems foundById(@PathVariable int requestId,
                                                  @RequestHeader(HttpHeaders.USER_ID) int userId) {
        return requestService.foundById(requestId, userId);
    }
}
