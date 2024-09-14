//package ru.practicum.server.booking.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.mockito.junit.jupiter.MockitoExtension;
//import ru.practicum.server.booking.dto.BookingResponse;
//import ru.practicum.server.booking.dto.CreateBookingRequest;
//import ru.practicum.server.booking.model.Booking;
//import ru.practicum.server.booking.model.Status;
//import ru.practicum.server.booking.repository.BookingRepository;
//import ru.practicum.server.item.dto.ItemResponse;
//import ru.practicum.server.item.model.Item;
//import ru.practicum.server.item.service.ItemService;
//import ru.practicum.server.user.dto.UserResponse;
//import ru.practicum.server.user.model.User;
//import ru.practicum.server.user.service.UserService;
//
//import java.time.LocalDateTime;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//public class BookingServiceImplTest {
//
//    @Mock
//    private BookingRepository bookingRepository;
//
//    @Mock
//    private ItemService itemService;
//
//    @Mock
//    private UserService userService;
//
//    @InjectMocks
//    private BookingServiceImpl bookingService;
//
//    private CreateBookingRequest bookingRequest;
//    private BookingResponse booking;
//    private UserResponse user; // Заменяем UserResponse на User для моков
//    private ItemResponse item; // Заменяем ItemResponse на Item для моков
//
//    @BeforeEach
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // Создаём тестовые данные
//        bookingRequest = new CreateBookingRequest();
//        bookingRequest.setItemId(1);
//        bookingRequest.setStart(LocalDateTime.now().plusDays(1));
//        bookingRequest.setEnd(LocalDateTime.now().plusDays(2));
//
//        booking = new BookingResponse();
//        booking.setId(1);
//        booking.setStatus(Status.WAITING);
//
//        user = new UserResponse(); // Используем User вместо UserResponse
//        user.setId(1);
//        user.setName("Test User");
//
//        item = new ItemResponse(); // Используем Item вместо ItemResponse
//        item.setId(1);
//        item.setIdOwner(user.getId()); // Владелец предмета - другой пользователь
//        item.setAvailable(true);
//    }
//
//    @Test
//    public void testCreateBooking() {
//        when(itemService.findItemById(anyInt(), anyInt())).thenReturn(item); // item, а не null
//        when(userService.getById(anyInt())).thenReturn(user);
//        when(bookingRepository.save(any(Booking.class))).thenReturn(booking);
//
//        // Вызываем метод сервиса
//        BookingResponse response = bookingService.createBooking(bookingRequest, user.getId());
//
//        // Проверки
//        assertNotNull(response);
//        assertEquals(Status.WAITING, response.getStatus());
//
//        verify(itemService, times(1)).findItemById(anyInt(), anyInt());
//        verify(userService, times(1)).getById(anyInt());
//        verify(bookingRepository, times(1)).save(any(Booking.class));
//    }
//}
