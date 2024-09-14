package ru.practicum.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateItemDto {
    @NotEmpty(message = "name is not be empty")
    private String name;

    @NotEmpty(message = "description is not be empty")
    private String description;

    @NotNull(message = "available is not be null")
    private Boolean available;

    private Integer requestId;
}
