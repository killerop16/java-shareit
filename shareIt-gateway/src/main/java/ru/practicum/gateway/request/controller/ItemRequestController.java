package ru.practicum.gateway.request.controller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.gateway.request.dto.CreateItemRequest;
import ru.practicum.gateway.request.dto.ItemRequestResponse;
import ru.practicum.gateway.request.dto.ItemRequestResponseWithItems;
import ru.practicum.gateway.util.HttpHeadersControllers;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final RestTemplate restTemplate;

    public ItemRequestController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }


    @PostMapping
    public ItemRequestResponse create(@Valid @RequestBody CreateItemRequest createRequest,
                                      @RequestHeader(HttpHeadersControllers.USER_ID) int userId) {

        String serverUrl = "http://localhost:9090/requests";
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));

        HttpEntity<CreateItemRequest> request = new HttpEntity<>(createRequest, headers);
        ResponseEntity<ItemRequestResponse> response = restTemplate.postForEntity(serverUrl, request, ItemRequestResponse.class);
        return response.getBody();
    }

    @GetMapping
    public List<ItemRequestResponseWithItems> foundRequestsById(@RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
        String serverUrl = "http://localhost:9090/requests";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<ItemRequestResponseWithItems>> response = restTemplate.exchange(serverUrl, HttpMethod.GET, entity,
                new ParameterizedTypeReference<>() {});

        return response.getBody();
    }

    @GetMapping("/all")
    public List<ItemRequestResponse> findAllRequests() {
        String serverUrl = "http://localhost:9090/requests/all";

        ResponseEntity<List<ItemRequestResponse>> response = restTemplate.exchange(serverUrl, HttpMethod.GET,
                null, new ParameterizedTypeReference<>() {});
        return response.getBody();
    }

    @GetMapping("/{requestId}")
    public ItemRequestResponseWithItems foundById(@PathVariable int requestId,
                                                  @RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
        String serverUrl = String.format("http://localhost:9090/requests/%d", requestId);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<ItemRequestResponseWithItems> response = restTemplate.exchange(serverUrl, HttpMethod.GET,
                entity, ItemRequestResponseWithItems.class);

        return response.getBody();
    }
}
