package ru.practicum.shareit.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    @Email
    private String email;
}
