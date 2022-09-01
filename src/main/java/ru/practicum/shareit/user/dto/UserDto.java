package ru.practicum.shareit.user.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import ru.practicum.shareit.item.model.Create;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Setter
@Getter
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {

    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @Email
    @NotNull(groups = {Create.class})
    private String email;
}
