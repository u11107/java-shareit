package ru.practicum.shareit.item;

import lombok.*;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.request.ItemRequest;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

@Data
@AllArgsConstructor
public class Item {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @NotNull
    @NotBlank
    private String description;
    @NotNull
    private Boolean available;
    private User owner;
    private ItemRequest request;
}
