package ru.practicum.gateway.request.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.gateway.client.ItemRequestClient;
import ru.practicum.gateway.request.dto.CreateItemRequest;
import ru.practicum.gateway.util.HttpHeadersControllers;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestClient itemRequestClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CreateItemRequest createRequest,
                                         @RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
        return itemRequestClient.post(userId, createRequest);
    }

    @GetMapping
    public ResponseEntity<Object> foundRequestsById(@RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
        return itemRequestClient.findByUserId(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> findAllRequests() {
        return itemRequestClient.findAll();
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> foundById(@PathVariable int requestId,
                                                  @RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
        return itemRequestClient.findById(requestId, userId);
    }
}
