package ru.practicum.gateway.request.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemRequest {
    @NotEmpty(message = "description is not be empty")
    private String description;
}
