package ru.practicum.gateway.booking.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.gateway.booking.dto.CreateBookingRequest;
import ru.practicum.gateway.client.BookingClient;
import ru.practicum.gateway.util.HttpHeadersControllers;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> bookItem(@RequestHeader(HttpHeadersControllers.USER_ID) long userId,
                                           @RequestBody @Valid CreateBookingRequest requestDto) {
        return bookingClient.bookItem(userId, requestDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> confirmOrRejectBooking(@PathVariable int bookingId,
                                                  @RequestParam boolean approved,
                                                  @RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
       return bookingClient.confirmOrRejectBooking(bookingId, userId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> findBookingById(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                           @PathVariable int bookingId) {
        return bookingClient.getBooking(userId, (long) bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> findUserReservationItems(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                                          @RequestParam(defaultValue = "ALL") String state) {
         return bookingClient.getOwnerBookings("", userId, state);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> findOwnerReservationItems(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                                           @RequestParam(defaultValue = "ALL") String state) {
        return bookingClient.getOwnerBookings("/owner", userId, state);
    }

}
