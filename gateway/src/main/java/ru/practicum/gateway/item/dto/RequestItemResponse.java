package ru.practicum.gateway.item.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RequestItemResponse {
    private int id;
    private int idOwner;
    private String name;
}
