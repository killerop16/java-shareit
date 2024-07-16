package ru.practicum.shareit.booking.repository;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Integer> {

    List<Booking> findAllByBookerId(int bookerId, Sort sort);

    List<Booking> findAllByBookerIdAndStartBeforeAndEndAfter(int booker_id, LocalDateTime s, LocalDateTime s1);

    List<Booking> findAllByBookerIdAndStatus(int bookerId, Status status, Sort sort);

    List<Booking> findALlByBookerIdAndStartAfter(int bookerId, LocalDateTime end, Sort sort);

    List<Booking> findAllByBookerIdAndEndBefore(int bookerId, LocalDateTime end, Sort sort);


    List<Booking> findAllByItemIdOwnerOrderByStartDesc(int ownerId);

    List<Booking> findAllByItemIdOwnerAndStatusOrderByStartDesc(int ownerId, Status status);


    List<Booking> findAllByItemIdOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(int ownerId, LocalDateTime start,
                                                                                    LocalDateTime end);
    List<Booking> findALLByItemIdOwnerAndStartIsAfterOrderByStartDesc(int ownerId, LocalDateTime start);

    List<Booking> findALLByItemIdOwnerAndEndIsBeforeOrderByStartDesc(int ownerId, LocalDateTime end);


    Optional<Booking> findFirstByItemIdAndStatusAndStartBeforeOrderByEndDesc(int itemId, Status status, LocalDateTime start);
    Optional<Booking> findFirstByItemIdAndStatusAndStartAfterOrderByStart(int itemId, Status status, LocalDateTime start);

    List<Booking> findAllByBookerIdAndItemIdAndEndBeforeAndStatus(int bookerId, int itemId,
                                                                      LocalDateTime end, Status status);
}
