package ru.practicum.shareit.user;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.Create;
import ru.practicum.shareit.item.Update;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class UserDto {
    private Long id;
    @NotBlank (groups = {Create.class})
    private String name;
    @Email(groups = {Update.class, Create.class})
    @NotNull(groups = {Create.class})
    private String email;
}
