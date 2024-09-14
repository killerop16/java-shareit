package ru.practicum.server.request.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.server.item.dto.RequestItemResponse;
import ru.practicum.server.item.model.Item;
import ru.practicum.server.item.repository.ItemRepository;
import ru.practicum.server.request.dto.CreateItemRequest;
import ru.practicum.server.request.dto.ItemRequestResponse;
import ru.practicum.server.request.dto.ItemRequestResponseWithItems;
import ru.practicum.server.request.model.ItemRequest;
import ru.practicum.server.request.repository.ItemRequestRepository;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private final ItemRequestRepository requestRepository;
    private final ModelMapper mapper;
    private final UserRepository userRepository;
    private final ItemRepository itemRepository;

    @Override
    public ItemRequestResponse create(CreateItemRequest itemRequestDto, int userId) {
        User user = findUserById(userId);

        ItemRequest itemRequest = mapper.map(itemRequestDto, ItemRequest.class);
        itemRequest.setCreated(LocalDateTime.now());
        itemRequest.setRequestor(user);
        log.info("create itemRequest");

        itemRequest = requestRepository.save(itemRequest);
        return mapper.map(itemRequest, ItemRequestResponse.class);
    }

    @Override
    public List<ItemRequestResponseWithItems> foundRequestsById(int userId) {
        User user = findUserById(userId);

        log.info("find itemsRequests by user with id: " + userId);
        List<ItemRequest> itemsRequests = requestRepository.findAllByRequestorOrderByCreatedDesc(user);

        log.info("find items by user with id: " + userId);
        List<Item> allItems = itemRepository.findItemsByIdOwnerOrderById(user);

        List<ItemRequestResponseWithItems> result = new ArrayList<>();
        for (ItemRequest itemRequest : itemsRequests) {
            ItemRequestResponseWithItems response = mapper.map(itemRequest, ItemRequestResponseWithItems.class);

            List<Item> filteredItems = allItems.stream()
                    .filter(item -> item.getItemRequest() != null && item.getItemRequest().getId() == itemRequest.getId())
                    .collect(Collectors.toList());

            response.setItems(mapper.map(filteredItems, new TypeToken<List<RequestItemResponse>>() {
            }.getType()));

            result.add(response);
        }
        return result;
    }

    @Override
    public List<ItemRequestResponse> findAll() {
        // Начинаем с сортировки по полю "created" в порядке убывания
        Sort sortByDate = Sort.by(Sort.Direction.DESC, "created");
        // Создаём описание первой страницы размером 32 элемента
        Pageable page = PageRequest.of(0, 3, sortByDate);
        List<ItemRequestResponse> itemRequestResponses = new ArrayList<>();
        do {
            // Запрашиваем у базы данных страницу с данными
            Page<ItemRequest> userPage = requestRepository.findAll(page);
            // Получаем содержимое страницы и преобразуем его в ItemRequestResponse
            userPage.getContent().forEach(itemRequest -> {
                itemRequestResponses.add(mapper.map(itemRequest, ItemRequestResponse.class));
            });
            // Проверяем, существует ли следующая страница
            if (userPage.hasNext()) {
                // Если следующая страница существует, создаём её описание
                page = userPage.nextPageable(); // Для удобства используем nextPageable()
            } else {
                page = null;
            }
        } while (page != null);
        return itemRequestResponses; // Возвращаем собранные данные
    }

    @Override
    public ItemRequestResponseWithItems foundById(int requestId, int userId) {
        findUserById(userId);

        log.info("Finding ItemRequest by id: {}", requestId);
        ItemRequest itemRequest = requestRepository.findById(requestId)
                .orElseThrow(() -> new EntityNotFoundException("ItemRequest not found with id: " + requestId));

        log.info("Finding items associated with request_id: {}", requestId);
        List<Item> allItems = itemRepository.findItemsByItemRequest(itemRequest);

        ItemRequestResponseWithItems response = mapper.map(itemRequest, ItemRequestResponseWithItems.class);

        // Фильтруем предметы, которые соответствуют запросу
        List<Item> filteredItems = allItems.stream()
                .filter(item -> requestId == item.getItemRequest().getId())
                .collect(Collectors.toList());

        response.setItems(mapper.map(filteredItems, new TypeToken<List<RequestItemResponse>>() {}.getType()));

        return response;
    }

    private User findUserById(int userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));
    }
}
