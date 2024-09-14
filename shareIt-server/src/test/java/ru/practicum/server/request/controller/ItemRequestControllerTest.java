package ru.practicum.server.request.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.server.item.dto.CreateItemDto;
import ru.practicum.server.request.dto.CreateItemRequest;
import ru.practicum.server.request.dto.ItemRequestResponse;
import ru.practicum.server.request.dto.ItemRequestResponseWithItems;
import ru.practicum.server.request.service.ItemRequestService;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(ItemRequestController.class)
public class ItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ItemRequestService itemRequestService;

    @Autowired
    private ObjectMapper objectMapper;

    private CreateItemRequest createItemRequest;
    private ItemRequestResponse itemRequestResponse;
    private ItemRequestResponseWithItems itemRequestResponseWithItems;

    @BeforeEach
    public void setUp() {
        createItemRequest = new CreateItemRequest();
        createItemRequest.setDescription("Test request");

        itemRequestResponse = new ItemRequestResponse();
        itemRequestResponse.setId(1);
        itemRequestResponse.setDescription("Test request");

        itemRequestResponseWithItems = new ItemRequestResponseWithItems();
        itemRequestResponseWithItems.setId(1);
        itemRequestResponseWithItems.setDescription("Test request");
        itemRequestResponseWithItems.setItems(Collections.emptyList());
    }

    @Test
    public void testCreateRequest() throws Exception {
        given(itemRequestService.create(any(CreateItemRequest.class), anyInt()))
                .willReturn(itemRequestResponse);

        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createItemRequest))
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test request"));
    }

    @Test
    public void testFoundRequestsById() throws Exception {
        given(itemRequestService.foundRequestsById(anyInt()))
                .willReturn(Collections.singletonList(itemRequestResponseWithItems));

        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Test request"));
    }

    @Test
    public void testFindAllRequests() throws Exception {
        given(itemRequestService.findAll())
                .willReturn(Collections.singletonList(itemRequestResponse));

        mockMvc.perform(get("/requests/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Test request"));
    }

    @Test
    public void testFoundById() throws Exception {
        given(itemRequestService.foundById(anyInt(), anyInt()))
                .willReturn(itemRequestResponseWithItems);

        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Test request"));
    }
}
