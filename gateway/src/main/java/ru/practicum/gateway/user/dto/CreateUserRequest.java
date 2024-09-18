package ru.practicum.gateway.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserRequest {
    @Size(max = 255)
    @NotEmpty
    private String name;

    @Size(max = 512)
    @NotEmpty
    @Email(message = "Invalid email format")
    private String email;
}
