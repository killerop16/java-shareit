package ru.practicum.server.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateItemRequest {
    private int id;

    @Size(max = 255)
    private String name;

    @Size(max = 1000)
    private String description;
    private Boolean available;
}
