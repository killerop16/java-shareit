package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.dto.BookingResponse;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.Status;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.modelException.NotFoundException;
import ru.practicum.shareit.exception.modelException.ValidationException;
import ru.practicum.shareit.item.comment.dto.CommentResponse;
import ru.practicum.shareit.item.comment.dto.CreateCommentRequest;
import ru.practicum.shareit.item.comment.model.Comment;
import ru.practicum.shareit.item.comment.repository.CommentRepository;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Item createItem(int userId, CreateItemRequest itemDto) {
        checkUserExist(userId);

        log.info("user '{}' create item", userId);
        Item item = mapper.map(itemDto, Item.class);
        item.setIdOwner(userId);
        item = itemRepository.save(item);
        return item;
    }

    @Override
    public Item updateItem(int userId, int itemId, UpdateItemRequest itemDto) {
        Item item = itemRepository.findItemByIdAndIdOwner(itemId, userId)
                .orElseThrow(() -> new NotFoundException("Item not found with id " + itemId
                        + "' for user with id: " + userId));

        item.setIdOwner(userId);

        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }

        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }

        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }

        log.info("Updating item with id '{}' for user with id '{}'", itemId, userId);
        return itemRepository.save(item);
    }

    @Override
    public ItemResponse findItemById(int itemId, int userId) {
        log.info("find item by id '{}'", itemId);
        Optional<Item> item = itemRepository.findById(itemId);
        Item foundItem = item.orElseThrow(() -> new NotFoundException("No User with such id"));

        return toItemResponse(foundItem, userId);
    }

    @Override
    public List<ItemResponse> findUserItemsById(int userId) {
        log.info("find user items by id '{}'", userId);

        return itemRepository.findItemsByIdOwnerOrderById(userId).stream()
                .map(item -> toItemResponse(item, userId))
                .collect(Collectors.toList());
    }

    /**
     * метод собирает ItemDto для ответа добавляет букинги и коментарии
     */
    private ItemResponse toItemResponse(Item item, int userId) {

        ItemResponse itemResponse = mapper.map(item, ItemResponse.class);
        if (item.getIdOwner() == userId) {
            Optional<Booking> last = bookingRepository.findFirstByItemIdAndStatusAndStartBeforeOrderByEndDesc(itemResponse.getId(),
                    Status.APPROVED, LocalDateTime.now());
            if (last.isPresent()) {
                BookingResponse lastBooking = mapper.map(last.get(), BookingResponse.class);
                itemResponse.setLastBooking(lastBooking);
            } else itemResponse.setLastBooking(null);


            Optional<Booking> next = bookingRepository.findFirstByItemIdAndStatusAndStartAfterOrderByStart(itemResponse.getId(),
                    Status.APPROVED, LocalDateTime.now());
            if (next.isPresent()) {
                BookingResponse nextBooking = mapper.map(next, BookingResponse.class);
                itemResponse.setNextBooking(nextBooking);
            } else itemResponse.setNextBooking(null);
        }

        List<CommentResponse> comments = commentRepository.findAllByItem(item).stream()
                .map(comment -> new CommentResponse(comment.getId(), comment.getText(),
                        comment.getAuthor().getName(), comment.getCreated()))
                .collect(Collectors.toList());


        itemResponse.setComments(comments);

        return itemResponse;
    }


    @Override
    public List<Item> findItemByText(int userId, String text) {
        if (text.isEmpty()) {
            return new ArrayList<>();
        }
        checkUserExist(userId);

        log.info("find item by text {}", text);
        return itemRepository.search(text);
    }

    @Override
    public CommentResponse createComment(int userId, int itemId, CreateCommentRequest commentDto) {
        var item = itemRepository.findById(itemId).orElseThrow(()
                -> new ValidationException("Item not found"));
        var user = userRepository.findById(userId).orElseThrow(()
                -> new ValidationException("User not found"));

        var bookings = bookingRepository.findAllByBookerIdAndItemIdAndEndBeforeAndStatus(userId,
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

    private void checkUserExist(int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }
}
