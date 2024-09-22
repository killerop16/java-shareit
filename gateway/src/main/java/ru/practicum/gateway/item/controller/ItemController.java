package ru.practicum.gateway.item.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.gateway.client.ItemClient;
import ru.practicum.gateway.item.comment.dto.CreateCommentRequest;
import ru.practicum.gateway.item.dto.CreateItem;
import ru.practicum.gateway.item.dto.UpdateItemRequest;
import ru.practicum.gateway.util.HttpHeadersControllers;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @PostMapping
    public ResponseEntity<Object> createItem(@Valid @RequestBody CreateItem itemDto,
                                             @RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
       return itemClient.post(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                   @PathVariable int itemId, @Valid @RequestBody UpdateItemRequest itemDto) {
        return itemClient.patch(itemId, userId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> findItemById(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                     @PathVariable int itemId) {
        return itemClient.findById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> findUserItemsById(@RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
        return itemClient.findItemsOfUser(userId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> findItemByText(@RequestHeader(HttpHeadersControllers.USER_ID) Long userId,
                                             @RequestParam("text") String text) {
        return itemClient.searchByText(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                         @PathVariable int itemId, @RequestBody CreateCommentRequest commentDto) {
        return itemClient.postComment(itemId, userId, commentDto);
    }
}
