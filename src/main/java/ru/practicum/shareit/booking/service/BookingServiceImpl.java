package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.CreateBookingRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.State;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.modelException.NotFoundException;
import ru.practicum.shareit.exception.modelException.ValidationException;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {
    private final ModelMapper mapper;
    private final BookingRepository bookingRepository;
    private final ItemService itemService;
    private final UserService userService;

    @Override
    public Booking createBooking(CreateBookingRequest bookingDto, int userId) {
        checkDtoExist(bookingDto, userId);

        log.info("user '{}' create booking", userId);

        ItemResponse item = itemService.findItemById(bookingDto.getItemId(), userId);
        if (item.getIdOwner() == userId) throw new NotFoundException("owner of the thing cannot rent it out to himself");
        if (item.getName() == null) throw new NotFoundException("Item has not be empty");

        Booking booking1 = new Booking();
        booking1.setStart(bookingDto.getStart());
        booking1.setEnd(bookingDto.getEnd());
        booking1.setItem(mapper.map(item, Item.class));
        booking1.setBooker(userService.getById(userId));
        booking1.setStatus(Status.WAITING);

        return bookingRepository.save(booking1);
    }

    private void checkDtoExist(CreateBookingRequest bookingDto, int userId) {
        checkUserExist(userId);
        checkItemExist(bookingDto.getItemId());

        LocalDateTime start = bookingDto.getStart();
        LocalDateTime end = bookingDto.getEnd();
        LocalDateTime now = LocalDateTime.now();

        if (start.isAfter(end) || start.equals(end) || start.isBefore(now))
            throw new ValidationException("End cannot be before start");
    }

    private void checkItemExist(Integer itemId) {
        if (!itemService.findItemById(itemId, 0).getAvailable()) {
            throw new ValidationException("Item is unavailable");
        }
    }

    private void checkUserExist(int userId) {
        userService.getById(userId);
    }

    @Override
    public Booking confirmOrRejectBooking(int bookingId, boolean approved, int userId) {
        checkUserExist(userId);

        log.info("Confirmation or rejection of the booking request.");
        Booking booking = findBookingById(userId, bookingId);
        if (booking.getItem().getIdOwner() != userId)
            throw new EntityNotFoundException("Only the owner of the item can change the status");
        if (booking.getStatus().equals(Status.APPROVED))
            throw new ValidationException("status cannot be changed to the same");
        if (approved) {
            booking.setStatus(Status.APPROVED);
        } else booking.setStatus(Status.REJECTED);

        return bookingRepository.save(booking);
    }

    @Override
    public Booking findBookingById(int userId, int bookingId) {
        log.info("find booking by UserId {}", userId);


        Optional<Booking> entity = bookingRepository.findById(bookingId);
        if (entity.isEmpty()) throw new EntityNotFoundException("Booking is not exist");

        Booking booking = entity.get();
        if (booking.getItem().getIdOwner() != userId && booking.getBooker().getId() != userId) {
            throw new EntityNotFoundException("This ID is not available for rent");
        }

        return booking;
    }

    @Override
    public List<Booking> findUserReservationItems(int userId, String state) {
        if (state.equals("UNSUPPORTED_STATUS")) throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        checkUserExist(userId);
        Sort sort = Sort.by(Sort.Direction.DESC, "start");

        switch (State.valueOf(state)) {
            case ALL:
                return bookingRepository.findAllByBookerId(userId, sort);
            case CURRENT:
                return bookingRepository.findAllByBookerIdAndStartBeforeAndEndAfter(userId, LocalDateTime.now(),
                        LocalDateTime.now());
            case PAST:
                return bookingRepository.findAllByBookerIdAndEndBefore(userId, LocalDateTime.now(), sort);
            case FUTURE:
                return bookingRepository.findALlByBookerIdAndStartAfter(userId, LocalDateTime.now(), sort);
            case WAITING:
                return bookingRepository.findAllByBookerIdAndStatus(userId, Status.WAITING, sort);
            case REJECTED:
                return bookingRepository.findAllByBookerIdAndStatus(userId, Status.REJECTED, sort);
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }

    @Override
    public List<Booking> findOwnerReservationItems(int ownerId, String state) {
        if (state.equals("UNSUPPORTED_STATUS")) throw new IllegalArgumentException("Unknown state: UNSUPPORTED_STATUS");
        checkUserExist(ownerId);

        switch (State.valueOf(state)) {
            case ALL:
                return bookingRepository.findAllByItemIdOwnerOrderByStartDesc(ownerId);
            case CURRENT:
                return bookingRepository.findAllByItemIdOwnerAndStartIsBeforeAndEndIsAfterOrderByStartDesc(ownerId,
                        LocalDateTime.now(), LocalDateTime.now());
            case PAST:
                return bookingRepository.findALLByItemIdOwnerAndEndIsBeforeOrderByStartDesc(ownerId, LocalDateTime.now());
            case FUTURE:
                return bookingRepository.findALLByItemIdOwnerAndStartIsAfterOrderByStartDesc(ownerId, LocalDateTime.now());
            case WAITING:
                return bookingRepository.findAllByItemIdOwnerAndStatusOrderByStartDesc(ownerId, Status.WAITING);
            case REJECTED:
                return bookingRepository.findAllByItemIdOwnerAndStatusOrderByStartDesc(ownerId, Status.REJECTED);
            default:
                throw new IllegalArgumentException("Unknown state: " + state);
        }
    }
}
