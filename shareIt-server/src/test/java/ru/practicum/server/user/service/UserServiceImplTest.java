//package ru.practicum.server.user.service;
//
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
//import ru.practicum.server.user.dto.CreateUserRequest;
//import ru.practicum.server.user.dto.UserResponse;
//import ru.practicum.server.user.model.User;
//import ru.practicum.server.user.repository.UserRepository;
//import ru.practicum.server.user.service.UserServiceImpl;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@SpringJUnitConfig
////@WebMvcTest(UserServiceImpl.class)
//@Import(UserServiceImpl.class)
//@DataJpaTest
//public class UserServiceImplTest {
//
//    @Autowired
//    private UserServiceImpl userService;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @BeforeEach
//    void setUp() {
//        userRepository.deleteAll(); // Очистка базы данных перед каждым тестом
//    }
//
//    @Test
//    void whenCreateUser_thenUserIsCreated() {
//        // Arrange
//        CreateUserRequest createUserRequest = new CreateUserRequest();
//        createUserRequest.setName("John Doe");
//        createUserRequest.setEmail("john.doe@example.com");
//
//        // Act
//        UserResponse userResponse = userService.create(createUserRequest);
//
//        // Assert
//        User user = userRepository.findById(userResponse.getId())
//                .orElseThrow(() -> new AssertionError("User not found"));
//
//        assertThat(user.getName()).isEqualTo("John Doe");
//        assertThat(user.getEmail()).isEqualTo("john.doe@example.com");
//        assertThat(userResponse.getName()).isEqualTo("John Doe");
//        assertThat(userResponse.getEmail()).isEqualTo("john.doe@example.com");
//    }
//}
