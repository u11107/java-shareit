package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;


@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {

    private Long id;
    private String name;
    @Email
    private String email;
}
