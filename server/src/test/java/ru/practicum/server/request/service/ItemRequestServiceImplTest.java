package ru.practicum.server.request.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import ru.practicum.server.item.dto.RequestItemResponse;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.request.dto.CreateItemRequest;
import ru.practicum.server.request.dto.ItemRequestResponse;
import ru.practicum.server.request.dto.ItemRequestResponseWithItems;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.repository.ItemRequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImplTest {

    @InjectMocks
    private ItemRequestServiceImpl itemRequestService;

    @Mock
    private ItemRequestRepository requestRepository;

    @Mock
    private ModelMapper mapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ItemRepository itemRepository;

    private int userId = 1;
    private int requestId = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTest() {
        CreateItemRequest itemRequestDto = new CreateItemRequest();
        itemRequestDto.setDescription("Test Request");

        User user = new User();
        user.setId(userId);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setRequestor(user);

        ItemRequestResponse itemRequestResponse = new ItemRequestResponse();
        itemRequestResponse.setId(requestId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.map(itemRequestDto, ItemRequest.class)).thenReturn(itemRequest);
        when(requestRepository.save(itemRequest)).thenReturn(itemRequest);
        when(mapper.map(itemRequest, ItemRequestResponse.class)).thenReturn(itemRequestResponse);

        ItemRequestResponse response = itemRequestService.create(itemRequestDto, userId);

        assertNotNull(response);
        assertEquals(requestId, response.getId());
        verify(requestRepository).save(itemRequest);
    }

    @Test
    void foundRequestsByIdTest() {
        User user = new User();
        user.setId(userId);

        ItemRequest itemRequest = new ItemRequest();
        itemRequest.setId(requestId);
        itemRequest.setRequestor(user);

        Item item = new Item();
        item.setItemRequest(itemRequest);

        List<ItemRequest> itemRequests = List.of(itemRequest);
        List<Item> items = List.of(item);

        ItemRequestResponseWithItems itemRequestResponseWithItems = new ItemRequestResponseWithItems();
        RequestItemResponse requestItemResponse = new RequestItemResponse();

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(requestRepository.findAllByRequestorOrderByCreatedDesc(user)).thenReturn(itemRequests);
        when(itemRepository.findItemsByIdOwnerOrderById(user)).thenReturn(items);
        when(mapper.map(itemRequest, ItemRequestResponseWithItems.class)).thenReturn(itemRequestResponseWithItems);
        when(mapper.map(any(), eq(new TypeToken<List<RequestItemResponse>>() {}.getType())))
                .thenReturn(List.of(requestItemResponse));

        List<ItemRequestResponseWithItems> responses = itemRequestService.foundRequestsById(userId);

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
        assertEquals(1, responses.size());
    }

    @Test
    void createTestThrowsEntityNotFoundException() {
        CreateItemRequest itemRequestDto = new CreateItemRequest();
        itemRequestDto.setDescription("Test Request");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            itemRequestService.create(itemRequestDto, userId);
        });

        assertEquals("User not found with id: " + userId, thrown.getMessage());
    }

    @Test
    void foundByIdTestThrowsEntityNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(requestRepository.findById(requestId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            itemRequestService.foundById(requestId, userId);
        });

        assertEquals("ItemRequest not found with id: " + requestId, thrown.getMessage());
    }
}
