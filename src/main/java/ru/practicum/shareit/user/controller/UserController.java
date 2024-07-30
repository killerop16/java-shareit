package ru.practicum.shareit.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public UserResponse update(@Valid @RequestBody UpdateUserRequest userDto,
                       @PathVariable int userId) {
        return userService.update(userId, userDto);
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @DeleteMapping("/{userId}")
    public void delUserById(@PathVariable int userId) {
        userService.delUserById(userId);
    }
}
