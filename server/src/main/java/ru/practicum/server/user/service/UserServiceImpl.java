package ru.practicum.server.user.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Service;
import ru.practicum.server.user.dto.CreateUserRequest;
import ru.practicum.server.user.dto.UpdateUserRequest;
import ru.practicum.server.user.dto.UserResponse;
import ru.practicum.server.user.model.User;
import ru.practicum.server.user.repository.UserRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public UserResponse create(CreateUserRequest userDto) {
        User user = mapper.map(userDto, User.class);
        log.info("create user by email {}", userDto.getEmail());

        user = userRepository.save(user);
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse update(int userId, UpdateUserRequest userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        String name = userDto.getName();
        if (name != null && !name.isBlank()) {
            existingUser.setName(userDto.getName());
        }

        String email = userDto.getEmail();
        if (email != null && !email.isBlank()) {
            existingUser.setEmail(userDto.getEmail());
        }
        log.info("update user by id {}", userId);

        User user = userRepository.save(existingUser);
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public UserResponse getById(int id) {
        log.info("find user by id {}", id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + id));
        return mapper.map(user, UserResponse.class);
    }

    @Override
    public List<UserResponse> findAll() {
        log.info("get all users");
        return mapper.map(userRepository.findAll(), new TypeToken<List<UserResponse>>(){}.getType());
    }


    @Override
    public void delUserById(int id) {
        log.info("delete user by id {}", id);
        userRepository.deleteById(id);
    }
}
