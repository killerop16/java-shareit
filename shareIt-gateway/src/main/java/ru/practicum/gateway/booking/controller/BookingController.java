package ru.practicum.gateway.booking.controller;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
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
import org.springframework.web.util.UriComponentsBuilder;
import ru.practicum.gateway.booking.dto.BookingResponse;
import ru.practicum.gateway.booking.dto.CreateBookingRequest;
import ru.practicum.gateway.util.HttpHeadersControllers;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping(path = "/bookings")
public class BookingController {
    private final RestTemplate restTemplate;

    public BookingController(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @PostMapping
    public BookingResponse createBooking(@Valid @RequestBody CreateBookingRequest bookingDto,
                                         @RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
        String serverUrl = "http://localhost:9090/bookings";

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));
        HttpEntity<CreateBookingRequest> request = new HttpEntity<>(bookingDto, headers);

        ResponseEntity<BookingResponse> response = restTemplate.postForEntity(serverUrl, request, BookingResponse.class);
        return response.getBody();
    }

    @PatchMapping("/{bookingId}")
    public BookingResponse confirmOrRejectBooking(@PathVariable int bookingId,
                                          @RequestParam boolean approved,
                                          @RequestHeader(HttpHeadersControllers.USER_ID) int userId) {
        String serverUrl = String.format("http://localhost:9090/bookings/%d?approved=%b", bookingId, approved);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));

        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);

        ResponseEntity<BookingResponse> response = restTemplate.exchange(
                serverUrl,
                HttpMethod.PUT,
                requestEntity,
                BookingResponse.class);

        return response.getBody();
    }

    @GetMapping("/{bookingId}")
    public BookingResponse findBookingById(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                    @PathVariable int bookingId) {
        String serverUrl = String.format("http://localhost:9090/bookings/%d", bookingId);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));
        HttpEntity<CreateBookingRequest> entity = new HttpEntity<>(headers);

        ResponseEntity<BookingResponse> response = restTemplate.exchange(serverUrl, HttpMethod.GET, entity, BookingResponse.class);

        return response.getBody();
    }

    @GetMapping()
    public List<BookingResponse> findUserReservationItems(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                                  @RequestParam(defaultValue = "ALL") String state) {
        String serverUrl = "http://localhost:9090/bookings";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(serverUrl)
                .queryParam("state", state);

        return getBookingResponses(userId, uriBuilder);
    }

    @GetMapping("/owner")
    public List<BookingResponse> findOwnerReservationItems(@RequestHeader(HttpHeadersControllers.USER_ID) int userId,
                                                   @RequestParam(defaultValue = "ALL") String state) {
        String serverUrl = "http://localhost:9090/bookings";

        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(serverUrl)
                .queryParam("state", state)
                .queryParam("ownerId", userId);

        return getBookingResponses(userId, uriBuilder);
    }

    private List<BookingResponse> getBookingResponses(@RequestHeader(HttpHeadersControllers.USER_ID) int userId, UriComponentsBuilder uriBuilder) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeadersControllers.USER_ID, String.valueOf(userId));

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<List<BookingResponse>> response = restTemplate.exchange(
                uriBuilder.toUriString(),
                HttpMethod.GET,
                entity,
                new ParameterizedTypeReference<>() {});

        return response.getBody();
    }

}
