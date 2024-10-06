package ru.practicum.gateway.user.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.gateway.client.UserClient;
import ru.practicum.gateway.user.dto.CreateUserRequest;
import ru.practicum.gateway.user.dto.UpdateUserRequest;

@RestController
@RequestMapping(path = "/users")
@RequiredArgsConstructor
public class UserController {
    private final UserClient userClient;

    @PostMapping
    public ResponseEntity<Object> create(@Valid @RequestBody CreateUserRequest userDto) {
        return userClient.post(userDto);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> update(@Valid @RequestBody UpdateUserRequest userDto,
                                          @PathVariable int userId) {
        return userClient.patch(userId, userDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getById(@PathVariable Integer id) {
        return userClient.findById(id);
    }

    @GetMapping
    public ResponseEntity<Object> findAll() {
        return userClient.findAll();
    }

    @DeleteMapping("/{userId}")
    public void delUserById(@PathVariable int userId) {
        userClient.delete(userId);
    }
}
