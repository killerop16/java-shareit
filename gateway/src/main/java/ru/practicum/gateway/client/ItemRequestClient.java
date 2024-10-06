package ru.practicum.gateway.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.request.dto.CreateItemRequest;

@Service
public class ItemRequestClient extends BaseClient {
    private static final String API_PREFIX = "/requests";

    @Autowired
    public ItemRequestClient(@Value("${shareit.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> post(long userId, CreateItemRequest itemRequestDtoCreate) {
        return post("", userId, itemRequestDtoCreate);
    }

    public ResponseEntity<Object> findByUserId(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findAll() {
        return get("/all");
    }

    public ResponseEntity<Object> findById(long itemRequestId, int userId) {
        return get("/" + itemRequestId, userId);
    }
}