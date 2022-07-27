package ru.practicum.shareit.user;

import lombok.Data;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    private String name;
    @Email
    private String email;
}
