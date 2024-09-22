package ru.practicum.gateway.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.user.dto.CreateUserRequest;
import ru.practicum.gateway.user.dto.UpdateUserRequest;

@Service
public class UserClient extends BaseClient {

    private static final String API_PREFIX = "/users";

    @Autowired
    public UserClient(@Value("${shareit.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> post(CreateUserRequest userDto) {
        return post("", userDto);
    }

    public ResponseEntity<Object> findAll() {
        return get("");
    }

    public ResponseEntity<Object> findById(long userId) {
        return get("/" + userId);
    }

    public ResponseEntity<Object> patch(long userId, UpdateUserRequest userDto) {
        return patch("/" + userId, userDto);
    }

    public ResponseEntity<Object> delete(long userId) {
        return delete("/" + userId);
    }
}

