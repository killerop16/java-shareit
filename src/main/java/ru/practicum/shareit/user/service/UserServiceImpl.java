package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.modelException.NotFoundException;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final ModelMapper mapper;

    @Override
    public User create(CreateUserRequest userDto) {
        User user = mapper.map(userDto, User.class);
        log.info("create user by email {}", userDto.getEmail());
        return userRepository.save(user);
    }

    @Override
    public User update(int userId, UpdateUserRequest userDto) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

        if (userDto.getName() != null) {
            existingUser.setName(userDto.getName());
        }

        if (userDto.getEmail() != null) {
            existingUser.setEmail(userDto.getEmail());
        }
        log.info("update user by id {}", userId);
        return userRepository.save(existingUser);
    }

    @Override
    public User getById(int id) {
        log.info("find user by id {}", id);
        Optional<User> user = userRepository.findById(id);
        if (user.isEmpty()) throw new NotFoundException("No User with such id");
        return user.get();
    }

    @Override
    public List<User> findAll() {
        log.info("get all users");
        return userRepository.findAll();
    }

    @Override
    public void delUserById(int id) {
        log.info("delete user by id {}", id);
        userRepository.deleteById(id);
    }
}
