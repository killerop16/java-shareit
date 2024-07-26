package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.comment.dto.CommentResponse;
import ru.practicum.shareit.item.comment.dto.CreateCommentRequest;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {
    ItemResponse createItem(int userId, CreateItemRequest itemDto);

    ItemResponse updateItem(int userId, int itemId, UpdateItemRequest itemDto);

    ItemResponse findItemById(int itemId, int userId);

    List<ItemResponse> findUserItemsById(int userId);

    List<ItemResponse> findItemByText(int userId, String text);

    CommentResponse createComment(int userId, int itemId, CreateCommentRequest commentDto);
}
