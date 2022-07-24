package ru.practicum.shareit.user.model;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class User {
    private Long id;
    @NotBlank
    @NotNull
    private String name;
    @NotNull
    @NotBlank
    @Email
    private String email;
}
