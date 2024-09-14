package ru.practicum.server.user.service;

import ru.practicum.server.user.dto.CreateUserRequest;
import ru.practicum.server.user.dto.UpdateUserRequest;
import ru.practicum.server.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(CreateUserRequest userDto);

    UserResponse update(int userId, UpdateUserRequest userDto);

    UserResponse getById(int id);

    List<UserResponse> findAll();

    void delUserById(int id);
}
