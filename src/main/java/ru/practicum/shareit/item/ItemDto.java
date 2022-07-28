package ru.practicum.shareit.item;

import lombok.Data;
import lombok.Getter;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
public class ItemDto {
    private Long id;
    @NotBlank(groups = {Create.class})
    private String name;
    @NotBlank(groups = {Create.class})
    private String description;
    @NotNull(groups = {Create.class})
    private Boolean available;
    private User owner;

    @Getter
    @AllArgsConstructor
    static class User {
        private Long id;
        private String name;
    }
}
