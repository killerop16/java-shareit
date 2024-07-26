package ru.practicum.shareit.item.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateCommentRequest {
    @NotBlank(message = "comment is not be empty text")
    @Size(min = 1, max = 512)
    private String text;
}
