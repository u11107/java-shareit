package ru.practicum.shareit.item.dto;

import lombok.Getter;
import lombok.AllArgsConstructor;

@Getter
@AllArgsConstructor
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;

    @Getter
    @AllArgsConstructor
    public static class User {
        private Long id;
        private String name;
    }
}
