package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.CreateUserRequest;
import ru.practicum.shareit.user.dto.UpdateUserRequest;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.validation.Valid;
import java.util.List;

/**
 * TODO Sprint add-controllers.
 */
@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
public class UserController {
    private UserService userService;

    @PostMapping
    public User create(@Valid @RequestBody CreateUserRequest userDto) {
        return userService.create(userDto);
    }

    @PatchMapping("/{userId}")
    public User update(@RequestBody UpdateUserRequest userDto,
                       @PathVariable int userId) {
        return userService.update(userId, userDto);
    }

    @GetMapping("/{id}")
    public User getById(@PathVariable Integer id) {
        return userService.getById(id);
    }

    @GetMapping
    public List<User> findAll() {
        return userService.findAll();
    }

    @DeleteMapping("/{userId}")
    public void delUserById(@PathVariable int userId) {
        userService.delUserById(userId);
    }
}
