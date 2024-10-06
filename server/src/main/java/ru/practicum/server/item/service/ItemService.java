package ru.practicum.server.item.service;

import ru.practicum.server.item.comment.dto.CommentResponse;
import ru.practicum.server.item.comment.dto.CreateCommentRequest;
import ru.practicum.server.item.dto.CreateItemDto;
import ru.practicum.server.item.dto.ItemResponse;
import ru.practicum.server.item.dto.UpdateItemRequest;

import java.util.List;

public interface ItemService {
    ItemResponse createItem(int userId, CreateItemDto itemDto);

    ItemResponse updateItem(int userId, int itemId, UpdateItemRequest itemDto);

    ItemResponse findItemById(int itemId, int userId);

    List<ItemResponse> findUserItemsById(int userId);

    List<ItemResponse> findItemByText(int userId, String text);

    CommentResponse createComment(int userId, int itemId, CreateCommentRequest commentDto);
}
