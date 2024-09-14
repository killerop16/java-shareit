package ru.practicum.server.item.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.server.item.dto.CreateItemDto;
import ru.practicum.server.item.dto.ItemResponse;
import ru.practicum.server.item.dto.UpdateItemRequest;
import ru.practicum.server.item.comment.dto.CommentResponse;
import ru.practicum.server.item.comment.dto.CreateCommentRequest;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.util.HttpHeaders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ItemController.class)
public class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemService itemService;

    @Autowired
    private ObjectMapper objectMapper;

    private ItemResponse itemResponse;
    private CreateItemDto createItemRequest;
    private UpdateItemRequest updateItemRequest;
    private CommentResponse commentResponse;
    private CreateCommentRequest createCommentRequest;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        itemResponse = new ItemResponse();
        itemResponse.setId(1);
        itemResponse.setName("Test Item");

        createItemRequest = new CreateItemDto();
        createItemRequest.setName("New Item");
        createItemRequest.setDescription("Item Description");
        createItemRequest.setAvailable(true);

        updateItemRequest = new UpdateItemRequest();
        updateItemRequest.setName("Updated Item");
        updateItemRequest.setDescription("Updated Description");
        updateItemRequest.setAvailable(false);

        createCommentRequest = new CreateCommentRequest();
        createCommentRequest.setText("Great item!");

        commentResponse = new CommentResponse();
        commentResponse.setId(1L);
        commentResponse.setText("Great item!");
    }

    @Test
    public void testCreateItem() throws Exception {
        when(itemService.createItem(anyInt(), any(CreateItemDto.class)))
                .thenReturn(itemResponse);

        mockMvc.perform(post("/items")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createItemRequest))
                        .header(HttpHeaders.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    public void testUpdateItem() throws Exception {
        when(itemService.updateItem(anyInt(), anyInt(), any(UpdateItemRequest.class)))
                .thenReturn(itemResponse);

        mockMvc.perform(patch("/items/1")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(updateItemRequest))
                        .header(HttpHeaders.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    public void testFindItemById() throws Exception {
        when(itemService.findItemById(anyInt(), anyInt()))
                .thenReturn(itemResponse);

        mockMvc.perform(get("/items/1")
                        .header(HttpHeaders.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    public void testFindUserItemsById() throws Exception {
        when(itemService.findUserItemsById(anyInt()))
                .thenReturn(List.of(itemResponse));

        mockMvc.perform(get("/items")
                        .header(HttpHeaders.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Item"));
    }

    @Test
    public void testFindItemByText() throws Exception {
        when(itemService.findItemByText(anyInt(), any(String.class)))
                .thenReturn(List.of(itemResponse));

        mockMvc.perform(get("/items/search")
                        .param("text", "Item")
                        .header(HttpHeaders.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Item"));
    }

    @Test
    public void testCreateComment() throws Exception {
        when(itemService.createComment(anyInt(), anyInt(), any(CreateCommentRequest.class)))
                .thenReturn(commentResponse);

        mockMvc.perform(post("/items/1/comment")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(createCommentRequest))
                        .header(HttpHeaders.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Great item!"));
    }
}
