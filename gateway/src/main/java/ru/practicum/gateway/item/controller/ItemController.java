package ru.practicum.gateway.item.controller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import ru.practicum.gateway.item.comment.dto.CommentResponse;
import ru.practicum.gateway.item.comment.dto.CreateCommentRequest;
import ru.practicum.gateway.item.dto.CreateItem;
import ru.practicum.gateway.item.dto.ItemResponse;
import ru.practicum.gateway.item.dto.UpdateItemRequest;
import ru.practicum.gateway.util.HttpHeadersControllers;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/items")
public class ItemController {
    private final RestTemplate restTemplate;

    public ItemController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @PostMapping
    public ItemResponse createItem(@Valid @RequestBody CreateItem itemDto,
                                   @RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
        String serverUrl = "http://localhost:9090/items";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));

        HttpEntity<CreateItem> request = new HttpEntity<>(itemDto, headers);

        ResponseEntity<ItemResponse> response = restTemplate.postForEntity(serverUrl, request, ItemResponse.class);
        return response.getBody();
    }

    @PatchMapping("/{itemId}")
    public ItemResponse updateItem(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                   @PathVariable int itemId, @Valid @RequestBody UpdateItemRequest itemDto) {
        String serverUrl = String.format("http://localhost:9090/items/%d", itemId);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));

        HttpEntity<UpdateItemRequest> request = new HttpEntity<>(itemDto, headers);
        ResponseEntity<ItemResponse> response = restTemplate.exchange(serverUrl, HttpMethod.PATCH, request, ItemResponse.class);
        return response.getBody();
    }

    @GetMapping("/{itemId}")
    public ItemResponse findItemById(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                     @PathVariable int itemId) {
        String serverUrl = String.format("http://localhost:9090/items/%d", itemId);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));

        HttpEntity<String> entity = new HttpEntity<>(headers);


        ResponseEntity<ItemResponse> response = restTemplate.exchange(serverUrl, HttpMethod.GET, entity, ItemResponse.class);
        return response.getBody();
    }

    @GetMapping
    public List<ItemResponse> findUserItemsById(@RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
        String serverUrl = "http://localhost:9090/items";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<ItemResponse>> response = restTemplate.exchange(serverUrl, HttpMethod.GET, entity, new ParameterizedTypeReference<>() {
        });

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No items found for user id: " + userId);
        }
        return response.getBody();
    }

    @GetMapping("/search")
    public List<ItemResponse> findItemByText(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                             @RequestParam("text") String text) {
        String serverUrl = String.format("http://localhost:9090/items/search?text=%s", text);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));

        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<ItemResponse>> response = restTemplate.exchange(serverUrl, HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {});

        if (response.getBody() == null || response.getBody().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No items found containing text: " + text);
        }

        return response.getBody();
    }

    @PostMapping("/{itemId}/comment")
    public CommentResponse createComment(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                         @PathVariable int itemId, @Valid @RequestBody CreateCommentRequest commentDto) {

        String serverUrl = String.format("http://localhost:9090/items/%d/comment", itemId);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));

        HttpEntity<CreateCommentRequest> entity = new HttpEntity<>(commentDto, headers);

        ResponseEntity<CommentResponse> response = restTemplate.exchange(serverUrl, HttpMethod.POST, entity, CommentResponse.class);

        if (response.getBody() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Comment could not be created");
        }
        return response.getBody();
    }
}
