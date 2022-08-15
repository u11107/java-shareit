package ru.practicum.shareit.user.dto;

import lombok.*;

import javax.validation.constraints.Email;

@Data
@AllArgsConstructor
public class UserDto {

    private Long id;
    private String name;
    @Email
    private String email;
}
