package ru.practicum.server.request.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CreateItemRequestTest {
    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCreateItemRequestSerialization() throws Exception {
        CreateItemRequest createItemRequest = new CreateItemRequest("Sample description");

        String jsonContent = objectMapper.writeValueAsString(createItemRequest);

        assertThat(jsonContent).contains("\"description\":\"Sample description\"");
    }

    @Test
    public void testCreateItemRequestValidation() {
        CreateItemRequest validRequest = new CreateItemRequest("Sample description");
        CreateItemRequest invalidRequest = new CreateItemRequest("");

        var validConstraints = validator.validate(validRequest);
        var invalidConstraints = validator.validate(invalidRequest);

        assertThat(validConstraints).isEmpty();
        assertThat(invalidConstraints).isNotEmpty();
    }
}
