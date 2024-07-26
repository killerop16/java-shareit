package ru.practicum.shareit.item.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.util.HttpHeaders;
import ru.practicum.shareit.item.comment.dto.CommentResponse;
import ru.practicum.shareit.item.comment.dto.CreateCommentRequest;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.service.ItemService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @PostMapping
    public ItemResponse createItem(@Valid @RequestBody CreateItemRequest itemDto,
                           @RequestHeader(HttpHeaders.USER_ID) int userId) {
        return itemService.createItem(userId, itemDto);
    }

    @PatchMapping("/{itemId}")
    public ItemResponse updateItem(@RequestHeader(HttpHeaders.USER_ID) int userId,
                           @PathVariable int itemId,
                           @RequestBody UpdateItemRequest itemDto) {
        return itemService.updateItem(userId, itemId, itemDto);
    }

    @GetMapping("/{itemId}")
    public ItemResponse findItemById(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                     @PathVariable int itemId) {
        return itemService.findItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemResponse> findUserItemsById(@RequestHeader(HttpHeaders.USER_ID) int userId) {
        return itemService.findUserItemsById(userId);
    }

    @GetMapping("/search")
    public List<ItemResponse> findItemByText(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                     @RequestParam("text") String text) {
        return itemService.findItemByText(userId, text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse createComment(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                         @PathVariable int itemId, @Valid @RequestBody CreateCommentRequest commentDto) {
        return itemService.createComment(userId, itemId, commentDto);
    }
}
