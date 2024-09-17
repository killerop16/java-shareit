package ru.practicum.server.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.server.user.dto.CreateUserRequest;
import ru.practicum.server.user.dto.UpdateUserRequest;
import ru.practicum.server.user.dto.UserResponse;
import ru.practicum.server.user.service.UserService;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public UserResponse create(@RequestBody CreateUserRequest userDto) {
        return userService.create(userDto);
    }

    @PutMapping("/{userId}")
    public UserResponse update(@RequestBody UpdateUserRequest userDto,
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
