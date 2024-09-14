package ru.practicum.gateway.user.controller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import ru.practicum.gateway.user.dto.CreateUserRequest;
import ru.practicum.gateway.user.dto.UpdateUserRequest;
import ru.practicum.gateway.user.dto.UserResponse;
import ru.practicum.gateway.util.HttpHeadersControllers;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final RestTemplate restTemplate;

    public UserController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @PostMapping
    public UserResponse create(@Valid @RequestBody CreateUserRequest userDto) {
        String serverUrl = "http://localhost:9090/users";
        HttpEntity<CreateUserRequest> request = new HttpEntity<>(userDto);
        ResponseEntity<UserResponse> response = restTemplate.postForEntity(serverUrl, request, UserResponse.class);
        return response.getBody();
    }

    @PatchMapping("/{userId}")
    public UserResponse update(@Valid @RequestBody UpdateUserRequest userDto,
                               @PathVariable int userId) {
        String serverUrl = String.format("http://localhost:9090/users/%d", userId);

        HttpEntity<UpdateUserRequest> request = new HttpEntity<>(userDto);
        try {
            ResponseEntity<UserResponse> response = restTemplate.exchange(serverUrl, HttpMethod.PUT, request, UserResponse.class);
            return response.getBody();
        } catch (HttpClientErrorException | HttpServerErrorException e) {
            System.err.println("Ошибка при запросе: " + e.getStatusCode() + " " + e.getResponseBodyAsString());
            throw new RuntimeException("Ошибка при обновлении пользователя: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Непредвиденная ошибка: " + e.getMessage());
            throw new RuntimeException("Ошибка при обновлении пользователя: " + e.getMessage());
        }
    }
    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable Integer id) {
        String serverUrl = "http://localhost:9090/users/{id}";
        ResponseEntity<UserResponse> response = restTemplate.getForEntity(serverUrl, UserResponse.class, id);
        return response.getBody();
    }

    @GetMapping
    public List<UserResponse> findAll() {
        String serverUrl = "http://localhost:9090/users";
        ResponseEntity<List<UserResponse>> response = restTemplate.exchange(serverUrl, HttpMethod.GET, null,
                new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    @DeleteMapping("/{userId}")
    public void delUserById(@PathVariable int userId) {
        String serverUrl = "http://localhost:9090/users/" + userId;
        restTemplate.delete(serverUrl);
    }
}
