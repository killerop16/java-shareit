package ru.practicum.server.booking.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.server.booking.dto.BookingResponse;
import ru.practicum.server.booking.dto.CreateBookingRequest;
import ru.practicum.server.booking.model.Status;
import ru.practicum.server.booking.service.BookingService;
import ru.practicum.server.util.HttpHeaders;

import java.time.LocalDateTime;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(BookingController.class)
public class BookingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookingService bookingService;

    @Autowired
    private ObjectMapper objectMapper;

    private BookingResponse bookingResponse;
    private CreateBookingRequest bookingRequest;

    @BeforeEach
    public void setUp() {
        bookingResponse = new BookingResponse();
        bookingResponse.setId(1);
        bookingResponse.setStatus(Status.WAITING);

        bookingRequest = new CreateBookingRequest();
        bookingRequest.setItemId(1);
        bookingRequest.setStart(LocalDateTime.now());
        bookingRequest.setEnd(LocalDateTime.now().plusDays(1));
    }

    // Тест на создание бронирования
    @Test
    public void testCreateBooking() throws Exception {
        when(bookingService.createBooking(any(CreateBookingRequest.class), anyInt()))
                .thenReturn(bookingResponse);

        // Используем ObjectMapper для конвертации объекта в JSON
        String bookingRequestJson = objectMapper.writeValueAsString(bookingRequest);

        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingRequestJson)
                        .header(HttpHeaders.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.status").value("WAITING"));
    }

    // Тест на подтверждение или отклонение бронирования
    @Test
    public void testConfirmOrRejectBooking() throws Exception {
        bookingResponse.setStatus(Status.APPROVED);

        when(bookingService.confirmOrRejectBooking(anyInt(), anyBoolean(), anyInt()))
                .thenReturn(bookingResponse);

        mockMvc.perform(patch("/bookings/1")
                        .param("approved", "true")
                        .header(HttpHeaders.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }

    // Тест на получение бронирования по ID
    @Test
    public void testFindBookingById() throws Exception {
        when(bookingService.findBookingById(anyInt(), anyInt()))
                .thenReturn(bookingResponse);

        mockMvc.perform(get("/bookings/1")
                        .header(HttpHeaders.USER_ID, 1))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    // Тест на получение всех бронирований пользователя
    @Test
    public void testFindUserReservationItems() throws Exception {
        when(bookingService.findUserReservationItems(anyInt(), anyString()))
                .thenReturn(Collections.singletonList(bookingResponse));

        mockMvc.perform(get("/bookings")
                        .header(HttpHeaders.USER_ID, 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    // Тест на получение всех бронирований владельца предметов
    @Test
    public void testFindOwnerReservationItems() throws Exception {
        when(bookingService.findOwnerReservationItems(anyInt(), anyString()))
                .thenReturn(Collections.singletonList(bookingResponse));

        mockMvc.perform(get("/bookings/owner")
                        .header(HttpHeaders.USER_ID, 1)
                        .param("state", "ALL"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }
}
