package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;

public interface BookingRepository extends JpaRepository<Booking, Integer> {
//
//    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.status = :state ORDER BY b.start DESC")
//    List<Booking> findAllByUserAndStateOrderByDateDesc(int userId, State state);
//
//    @Query("SELECT b FROM Booking b WHERE b.booker.id = :userId AND b.status = :state ORDER BY b.start DESC")
//    List<Booking> findOwnerReservationItems(int userId, State state);

    List<Booking> findAllByBookerIdAndStatus(int bookerId, Status status, Sort sort);
    List<Booking> findAllByBookerIdAndEndBefore(int bookerId, LocalDateTime end, Sort sort);
    List<Booking> findALlByBookerIdAndStartAfter(int bookerId, LocalDateTime end, Sort sort);
  //  List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(int booker_id, LocalDateTime start);
    List<Booking> findAllByBookerId(int bookerId, Sort sort);
}
