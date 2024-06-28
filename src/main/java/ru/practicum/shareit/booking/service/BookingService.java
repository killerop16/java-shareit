package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;

import java.util.List;

public interface BookingService {
    Booking createBooking(CreateBookingRequest bookingDto, int userId);

    Booking confirmOrRejectBooking(int bookingId, boolean approved, int userId);

    Booking findBookingById(int userId, int bookingId);

   List<Booking> findUserReservationItems(int userId, State state);

   List<Booking> findOwnerReservationItems(int userId, State state);
}
