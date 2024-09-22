package ru.practicum.gateway.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.gateway.item.comment.dto.CreateCommentRequest;
import ru.practicum.gateway.item.dto.CreateItem;
import ru.practicum.gateway.item.dto.UpdateItemRequest;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit.server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .requestFactory(() -> new HttpComponentsClientHttpRequestFactory())
                        .build()
        );
    }

    public ResponseEntity<Object> post(long userId, CreateItem itemDtoCreate) {
        return post("", userId, itemDtoCreate);
    }

    public ResponseEntity<Object> findItemsOfUser(long userId) {
        return get("", userId);
    }

    public ResponseEntity<Object> findById(long itemId, int userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> patch(long itemId, long userId, UpdateItemRequest itemDto) {
        return patch("/" + itemId, userId, itemDto);
    }

    public ResponseEntity<Object> delete(long itemId, long userId) {
        return delete("/" + itemId, userId);
    }

    public ResponseEntity<Object> searchByText(String text, Long userId) {
        Map<String, Object> parameters = Map.of(
                "text", text
        );
        return get(addParamToPath("/search", parameters), userId, parameters);
    }

    public ResponseEntity<Object> postComment(long itemId, long userId, CreateCommentRequest commentDto) {
        return post(String.format("/%s/comment", itemId), userId, commentDto);
    }
}