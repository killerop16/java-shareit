package ru.practicum.server.booking.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.server.booking.dto.BookingResponse;
import ru.practicum.server.booking.dto.CreateBookingRequest;
import ru.practicum.server.booking.service.BookingService;
import ru.practicum.server.util.HttpHeaders;

import java.util.List;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
public class BookingController {
    private final BookingService bookingService;

    @PostMapping
    public BookingResponse createBooking(@RequestBody CreateBookingRequest bookingDto,
                                         @RequestHeader(HttpHeaders.USER_ID) int userId) {
        return bookingService.createBooking(bookingDto, userId);
    }

    @PutMapping("/{bookingId}")
    public BookingResponse confirmOrRejectBooking(@PathVariable int bookingId,
                                          @RequestParam boolean approved,
                                          @RequestHeader(HttpHeaders.USER_ID) int userId) {
        return bookingService.confirmOrRejectBooking(bookingId, approved, userId);
    }

    @GetMapping("/{bookingId}")
    public BookingResponse findBookingById(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                    @PathVariable int bookingId) {
        return bookingService.findBookingById(userId, bookingId);
    }

    @GetMapping()
    public List<BookingResponse> findUserReservationItems(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findUserReservationItems(userId, state);
    }

    @GetMapping("/owner")
    public List<BookingResponse> findOwnerReservationItems(@RequestHeader(HttpHeaders.USER_ID) int userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        return bookingService.findOwnerReservationItems(userId, state);
    }
}
