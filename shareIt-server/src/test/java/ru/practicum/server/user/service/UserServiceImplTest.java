package ru.practicum.server.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import ru.practicum.server.user.dto.CreateUserRequest;
import ru.practicum.server.user.dto.UpdateUserRequest;
import ru.practicum.server.user.dto.UserResponse;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@Slf4j
@RequiredArgsConstructor
public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper mapper;

    private int userId = 1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createTest() {
        CreateUserRequest createUserRequest = new CreateUserRequest();
        createUserRequest.setName("John Doe");
        createUserRequest.setEmail("john.doe@example.com");

        User user = new User();
        user.setId(userId);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);

        when(mapper.map(createUserRequest, User.class)).thenReturn(user);
        when(userRepository.save(user)).thenReturn(user);
        when(mapper.map(user, UserResponse.class)).thenReturn(userResponse);

        UserResponse response = userService.create(createUserRequest);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        verify(userRepository).save(user);
    }

    @Test
    void getByIdTest() {
        User user = new User();
        user.setId(userId);

        UserResponse userResponse = new UserResponse();
        userResponse.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(mapper.map(user, UserResponse.class)).thenReturn(userResponse);

        UserResponse response = userService.getById(userId);

        assertNotNull(response);
        assertEquals(userId, response.getId());
        verify(userRepository).findById(userId);
    }

    @Test
    void delUserByIdTest() {
        doNothing().when(userRepository).deleteById(userId);

        userService.delUserById(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void updateTestThrowsEntityNotFoundException() {
        UpdateUserRequest updateUserRequest = new UpdateUserRequest();
        updateUserRequest.setName("Jane Doe");

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            userService.update(userId, updateUserRequest);
        });

        assertEquals("User not found with id: " + userId, thrown.getMessage());
    }

    @Test
    void getByIdTestThrowsEntityNotFoundException() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        EntityNotFoundException thrown = assertThrows(EntityNotFoundException.class, () -> {
            userService.getById(userId);
        });

        assertEquals("User not found with id: " + userId, thrown.getMessage());
    }
}
