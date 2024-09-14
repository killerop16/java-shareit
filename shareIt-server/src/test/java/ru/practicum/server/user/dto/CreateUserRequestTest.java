package ru.practicum.server.user.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CreateUserRequestTest {

    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeCreateUserRequest() throws Exception {
        CreateUserRequest createUserRequest = new CreateUserRequest("John Doe", "john.doe@example.com");

        String json = objectMapper.writeValueAsString(createUserRequest);

        assertThat(json).contains("\"name\":\"John Doe\"");
        assertThat(json).contains("\"email\":\"john.doe@example.com\"");
    }

    @Test
    public void testDeserializeCreateUserRequest() throws Exception {
        String json = "{\"name\":\"John Doe\",\"email\":\"john.doe@example.com\"}";

        CreateUserRequest createUserRequest = objectMapper.readValue(json, CreateUserRequest.class);

        assertThat(createUserRequest.getName()).isEqualTo("John Doe");
        assertThat(createUserRequest.getEmail()).isEqualTo("john.doe@example.com");
    }
}