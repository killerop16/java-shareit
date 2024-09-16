package ru.practicum.server.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import ru.practicum.server.booking.dto.BookingResponseDepends;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.booking.repository.BookingRepository;
import ru.practicum.server.exception.modelException.NotFoundException;
import ru.practicum.server.exception.modelException.ValidationException;
import ru.practicum.server.item.comment.dto.CommentResponse;
import ru.practicum.server.item.comment.dto.CreateCommentRequest;
import ru.practicum.server.item.comment.model.Comment;
import ru.practicum.server.item.comment.repository.CommentRepository;
import ru.practicum.server.item.dto.CreateItemDto;
import ru.practicum.server.item.dto.ItemResponse;
import ru.practicum.server.item.dto.UpdateItemRequest;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@Slf4j
@RequiredArgsConstructor
public class ItemServiceImplTest {

    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private BookingRepository bookingRepository;

    private int userId = 1;
    private int itemId = 1;
    private int bookingId = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createItemTest() {
        CreateItemDto itemDto = new CreateItemDto();
        itemDto.setName("Test Item");
        itemDto.setDescription("Test Description");
        itemDto.setAvailable(true);

        User user = new User();
        user.setId(userId);

        Item item = new Item();
        item.setIdOwner(user);

        ItemResponse itemResponse = new ItemResponse();
        itemResponse.setId(itemId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.map(itemDto, Item.class)).thenReturn(item);
        when(itemRepository.save(item)).thenReturn(item);
        when(mapper.map(item, ItemResponse.class)).thenReturn(itemResponse);

        ItemResponse response = itemService.createItem(userId, itemDto);

        assertNotNull(response);
        assertEquals(itemId, response.getId());
        verify(itemRepository).save(item);
    }

    @Test
    void findItemByIdTest() {
        Item item = new Item();
        item.setId(itemId);
        item.setIdOwner(new User());

        CommentResponse commentResponse = new CommentResponse();
        BookingResponseDepends bookingResponseDepends = new BookingResponseDepends();

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(commentRepository.findAllByItems(Collections.singletonList(item)))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.findAllLastBookingsByItemsAndStatus(
                Collections.singletonList(itemId), Status.APPROVED, LocalDateTime.now()))
                .thenReturn(Collections.emptyList());
        when(bookingRepository.findAllNextBookingsByItemsAndStatus(
                Collections.singletonList(itemId), Status.APPROVED, LocalDateTime.now()))
                .thenReturn(Collections.emptyList());
        when(mapper.map(any(), eq(ItemResponse.class))).thenReturn(new ItemResponse());

        ItemResponse response = itemService.findItemById(itemId, userId);

        assertNotNull(response);
        verify(itemRepository).findById(itemId);
    }

    @Test
    void createCommentTestThrowsValidationException() {
        CreateCommentRequest commentDto = new CreateCommentRequest();
        commentDto.setText("Test Comment");

        Item item = new Item();
        item.setId(itemId);

        when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new User()));
        when(bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(
                anyInt(), anyInt(), any(LocalDateTime.class), eq(Status.APPROVED)))
                .thenReturn(Collections.emptyList()); // Assuming no rental record

        ValidationException thrown = assertThrows(ValidationException.class, () -> {
            itemService.createComment(userId, itemId, commentDto);
        });

        assertEquals("This user did not rent this item", thrown.getMessage());
    }
}
