package ru.practicum.server.item.dto;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CreateItemDtoTest {
    private ObjectMapper objectMapper;
    private Validator validator;

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testCreateItemDtoSerialization() throws Exception {
        CreateItemDto createItemDto = new CreateItemDto("Item Name", "Item Description", true, 123);

        String jsonContent = objectMapper.writeValueAsString(createItemDto);

        assertThat(jsonContent).contains("\"name\":\"Item Name\"");
        assertThat(jsonContent).contains("\"description\":\"Item Description\"");
        assertThat(jsonContent).contains("\"available\":true");
        assertThat(jsonContent).contains("\"requestId\":123");
    }

    @Test
    public void testCreateItemDtoValidation() {
        CreateItemDto validDto = new CreateItemDto("Valid Name", "Valid Description", true, 123);
        CreateItemDto invalidDto = new CreateItemDto("", "", null, null);

        var validConstraints = validator.validate(validDto);
        var invalidConstraints = validator.validate(invalidDto);

        assertThat(validConstraints).isEmpty();
        assertThat(invalidConstraints).isNotEmpty();
        assertThat(invalidConstraints).extracting("message")
                .contains("name is not be empty", "description is not be empty", "available is not be null");
    }
}
