package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.modelException.NotFoundException;
import ru.practicum.shareit.item.dto.CreateItemRequest;
import ru.practicum.shareit.item.dto.UpdateItemRequest;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ModelMapper mapper;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

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
//        item.setRequest(itemDto.getR);

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
    public Item findItemById(int itemId) {
        log.info("find item by id '{}'", itemId);
        Optional<Item> item = itemRepository.findById(itemId);
        if (item.isEmpty()) throw new NotFoundException("No User with such id");
        return item.get();
    }

    @Override
    public List<Item> findUserItemsById(int userId) {
        log.info("find user items by id '{}'", userId);
        return itemRepository.findUserItemsById(userId);
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

    private void checkUserExist(int userId) {
        userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }
}
