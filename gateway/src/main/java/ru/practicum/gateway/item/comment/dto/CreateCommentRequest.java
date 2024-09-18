package ru.practicum.gateway.item.comment.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateCommentRequest {
    @NotBlank(message = "comment is not be empty text")
    @Size(min = 1, max = 512)
    private String text;
}
