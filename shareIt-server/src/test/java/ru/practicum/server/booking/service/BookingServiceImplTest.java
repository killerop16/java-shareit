package ru.practicum.server.booking.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import ru.practicum.server.booking.dto.BookingResponse;
import ru.practicum.server.booking.model.Booking;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.booking.repository.BookingRepository;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.service.ItemService;
import ru.practicum.server.user.dto.UserResponse;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BookingServiceImplTest {

    @InjectMocks
    private BookingServiceImpl bookingService;

    @Mock
    private ModelMapper mapper;

    @Mock
    private BookingRepository bookingRepository;

    @Mock
    private ItemService itemService;

    @Mock
    private UserService userService;

    private int userId = 1;
    private int itemId = 1;
    private int bookingId = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void confirmOrRejectBookingTest() {
        Booking booking = new Booking();
        booking.setItem(new Item());
        booking.getItem().setIdOwner(new User());
        booking.getItem().getIdOwner().setId(userId);
        booking.setStatus(Status.WAITING);

        BookingResponse bookingResponse = new BookingResponse();
        bookingResponse.setStatus(Status.APPROVED);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);

        when(bookingRepository.findById(bookingId)).thenReturn(Optional.of(booking));
        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
        when(mapper.map(any(Booking.class), eq(BookingResponse.class))).thenReturn(bookingResponse);
        when(userService.getById(userId)).thenReturn(userResponse);

        BookingResponse response = bookingService.confirmOrRejectBooking(bookingId, true, userId);

        assertNotNull(response);
        assertEquals(Status.APPROVED, response.getStatus());
        verify(bookingRepository).save(any(Booking.class));
    }

    @Test
    void findUserReservationItemsTest() {
        Booking booking = new Booking();
        booking.setStart(LocalDateTime.now().plusDays(1));
        booking.setEnd(LocalDateTime.now().plusDays(2));

        BookingResponse bookingResponse = new BookingResponse();

        when(bookingRepository.findAllByBookerId(anyInt(), any())).thenReturn(List.of(booking));
        when(mapper.map(any(), eq(new TypeToken<List<BookingResponse>>() {}.getType()))).thenReturn(List.of(bookingResponse));

        List<BookingResponse> responses = bookingService.findUserReservationItems(userId, "ALL");

        assertNotNull(responses);
        assertFalse(responses.isEmpty());
    }

}
