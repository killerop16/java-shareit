//package ru.practicum.server.item.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import org.springframework.transaction.annotation.Transactional;
//import ru.practicum.server.item.dto.CreateItemDto;
//import ru.practicum.server.item.dto.ItemResponse;
//import ru.practicum.server.item.dto.UpdateItemRequest;
//import ru.practicum.server.item.model.Item;
//import ru.practicum.server.item.repository.ItemRepository;
//import ru.practicum.server.user.model.User;
//import ru.practicum.server.user.repository.UserRepository;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//@SpringJUnitConfig
//@DataJpaTest
//@Transactional
//public class ItemServiceImplTest {
//
//    @Autowired
//    private ItemRepository itemRepository;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private ItemServiceImpl itemService;
//
//    private User user;
//    private Item item;
//
//    @BeforeEach
//    void setUp() {
//        user = new User();
//        user.setName("Test User");
//        user = userRepository.save(user);
//
//        item = new Item();
//        item.setName("Test Item");
//        item.setDescription("Test Description");
//        item.setAvailable(true);
//        item.setIdOwner(user);
//        item = itemRepository.save(item);
//    }
//
//    @Test
//    void testCreateItem() {
//        CreateItemDto createItemDto = new CreateItemDto();
//        createItemDto.setName("New Item");
//        createItemDto.setDescription("New Description");
//        createItemDto.setAvailable(true);
//
//        ItemResponse response = itemService.createItem(user.getId(), createItemDto);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getName()).isEqualTo(createItemDto.getName());
//        assertThat(response.getDescription()).isEqualTo(createItemDto.getDescription());
//    }
//
//    @Test
//    void testUpdateItem() {
//        UpdateItemRequest updateItemRequest = new UpdateItemRequest();
//        updateItemRequest.setName("Updated Name");
//        updateItemRequest.setDescription("Updated Description");
//        updateItemRequest.setAvailable(false);
//
//        ItemResponse response = itemService.updateItem(user.getId(), item.getId(), updateItemRequest);
//
//        assertThat(response).isNotNull();
//        assertThat(response.getName()).isEqualTo(updateItemRequest.getName());
//        assertThat(response.getDescription()).isEqualTo(updateItemRequest.getDescription());
//        assertThat(response.getAvailable()).isEqualTo(updateItemRequest.getAvailable());
//    }
//
//    @Test
//    void testFindItemById() {
//        ItemResponse response = itemService.findItemById(item.getId(), user.getId());
//
//        assertThat(response).isNotNull();
//        assertThat(response.getId()).isEqualTo(item.getId());
//    }
//
//    @Test
//    void testFindUserItemsById() {
//        var responses = itemService.findUserItemsById(user.getId());
//
//        assertThat(responses).isNotEmpty();
//        assertThat(responses.size()).isGreaterThan(0);
//    }
//
//    @Test
//    void testFindItemByText() {
//        var responses = itemService.findItemByText(user.getId(), "Test Item");
//
//        assertThat(responses).isNotEmpty();
//        assertThat(responses.size()).isGreaterThan(0);
//    }
//}
