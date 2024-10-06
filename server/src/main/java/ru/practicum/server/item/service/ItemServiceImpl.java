package ru.practicum.server.item.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import ru.practicum.server.booking.dto.BookingResponseDepends;
import ru.practicum.server.booking.model.Booking;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ModelMapper mapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final BookingRepository bookingRepository;

    @Override
    public ItemResponse createItem(int userId, CreateItemDto itemDto) {
        User user = checkUserExist(userId);

        log.info("user '{}' create item", userId);

        Item item = mapper.map(itemDto, Item.class);
        item.setIdOwner(user);
        if (item.getItemRequest() != null) {
            item.setItemRequest(item.getItemRequest());
        } else item.setItemRequest(null);
        item = itemRepository.save(item);
        return mapper.map(item, ItemResponse.class);
    }

    @Override
    public ItemResponse updateItem(int userId, int itemId, UpdateItemRequest itemDto) {
        User user = checkUserExist(userId);

        log.info("Find item with id " + itemId + " for user with id: " + userId);
        Item item = checkItemExist(itemId);

        item.setIdOwner(user);

        String name = itemDto.getName();
        if (name != null && !name.isBlank()) {
            item.setName(itemDto.getName());
        }

        String description = itemDto.getDescription();
        if (description != null && !description.isBlank()) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        log.info("Updating item with id '{}' for user with id '{}'", itemId, userId);
        item = itemRepository.save(item);
        return mapper.map(item, ItemResponse.class);
    }

    @Override
    public ItemResponse findItemById(int itemId, int userId) {
        log.info("find item by id '{}'", itemId);
        Item foundItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("No Item with such id"));

        List<Item> items = Collections.singletonList(foundItem); // Оборачиваем в список

        Map<Integer, List<CommentResponse>> commentsMap = commentRepository.findAllByItems(items).stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId(),
                        Collectors.mapping(comment -> new CommentResponse(comment.getId(), comment.getText(),
                                        comment.getAuthor().getName(), comment.getCreated()),
                                Collectors.toList())));

        Map<Integer, BookingResponseDepends> lastBookingsMap = bookingRepository.findAllLastBookingsByItemsAndStatus(
                        Collections.singletonList(itemId),
                        Status.APPROVED, LocalDateTime.now()).stream()
                .collect(Collectors.toMap(
                        booking -> booking.getItem().getId(),
                        booking -> mapper.map(booking, BookingResponseDepends.class),
                        (existing, replacement) -> replacement // Обновляем значение
                ));

        Map<Integer, BookingResponseDepends> nextBookingsMap = bookingRepository.findAllNextBookingsByItemsAndStatus(
                        Collections.singletonList(itemId),
                        Status.APPROVED, LocalDateTime.now()).stream()
                .collect(Collectors.toMap(
                        booking -> booking.getItem().getId(),
                        booking -> mapper.map(booking, BookingResponseDepends.class),
                        (existing, replacement) -> replacement // Обновляем значение
                ));


        return toItemResponse(foundItem, userId, commentsMap, lastBookingsMap, nextBookingsMap);

    }

    @Override
    public List<ItemResponse> findUserItemsById(int userId) {
        log.info("find user items by id '{}'", userId);
        User user = checkUserExist(userId);

        List<Item> items = itemRepository.findItemsByIdOwnerOrderById(user);
        Map<Integer, List<CommentResponse>> commentsMap = commentRepository.findAllByItems(items).stream()
                .collect(Collectors.groupingBy(comment -> comment.getItem().getId(),
                        Collectors.mapping(comment -> new CommentResponse(comment.getId(), comment.getText(),
                                        comment.getAuthor().getName(), comment.getCreated()),
                                Collectors.toList())));

        // Получение последних бронирований с обновлением значений при дубликатах
        Map<Integer, BookingResponseDepends> lastBookingsMap = bookingRepository.findAllLastBookingsByItemsAndStatus(
                        items.stream().map(Item::getId).collect(Collectors.toList()),
                        Status.APPROVED, LocalDateTime.now()).stream()
                .collect(Collectors.toMap(
                        booking -> booking.getItem().getId(),
                        booking -> mapper.map(booking, BookingResponseDepends.class),
                        (existing, replacement) -> replacement // Обновляем значение при дубликатах
                ));

        // Получение следующих бронирований с обновлением значений при дубликатах
        Map<Integer, BookingResponseDepends> nextBookingsMap = bookingRepository.findAllNextBookingsByItemsAndStatus(
                        items.stream().map(Item::getId).collect(Collectors.toList()),
                        Status.APPROVED, LocalDateTime.now()).stream()
                .collect(Collectors.toMap(
                        booking -> booking.getItem().getId(),
                        booking -> mapper.map(booking, BookingResponseDepends.class),
                        (existing, replacement) -> replacement)); // Обновляем значение при дубликатах

        return items.stream()
                .map(item -> toItemResponse(item, userId, commentsMap, lastBookingsMap, nextBookingsMap))
                .collect(toList());
    }

    /**
     * Метод собирает ItemDto для ответа добавляет букинги и коменты
     */
    private ItemResponse toItemResponse(Item item, int userId,
                                        Map<Integer, List<CommentResponse>> commentsMap,
                                        Map<Integer, BookingResponseDepends> lastBookingsMap,
                                        Map<Integer, BookingResponseDepends> nextBookingsMap) {

        ItemResponse itemResponse = mapper.map(item, ItemResponse.class);

        if (item.getIdOwner().getId() == userId) {
            itemResponse.setLastBooking(lastBookingsMap.getOrDefault(itemResponse.getId(), null));
            itemResponse.setNextBooking(nextBookingsMap.getOrDefault(itemResponse.getId(), null));
        }

        List<CommentResponse> comments = commentsMap.getOrDefault(item.getId(), Collections.emptyList());
        itemResponse.setComments(comments);

        return itemResponse;
    }

    @Override
    public List<ItemResponse> findItemByText(int userId, String text) {
        if (text.isBlank()) {
            return List.of();
        }
        checkUserExist(userId);

        log.info("find item by text {}", text);
        List<Item> items = itemRepository.search(text);
        return mapper.map(items, new TypeToken<List<ItemResponse>>() {
        }.getType());
    }

    @Override
    public CommentResponse createComment(int userId, int itemId, CreateCommentRequest commentDto) {
        Item item = checkItemExist(itemId);
        User user = checkUserExist(userId);

        List<Booking> bookings = bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(userId,
                itemId, LocalDateTime.now(), Status.APPROVED);
        if (bookings.isEmpty()) {
            throw new ValidationException("This user did not rent this item");
        }

        log.info("user '{}' create comment by Item {}", userId, itemId);
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setAuthor(user);
        comment.setCreated(LocalDateTime.now());
        comment.setItem(item);
        comment = commentRepository.save(comment);

        return new CommentResponse(comment.getId(), comment.getText(),
                comment.getAuthor().getName(), comment.getCreated());
    }

    /**
     * Метод проверяет наличие item и возвращает в случаи если он есть
     */
    private Item checkItemExist(int itemId) {
        return itemRepository.findById(itemId).orElseThrow(()
                -> new ValidationException("Item not found"));
    }


    /**
     * Метод проверяет наличие user и возвращает в случаи если он есть
     */
    private User checkUserExist(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }

}
