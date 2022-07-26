package ru.practicum.shareit.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.requests.ItemRequest;
import ru.practicum.shareit.user.model.User;

@Data
@AllArgsConstructor
public class Item {
    private long id;
    private String name;
    private String description;
    private Boolean available;
    private User owner;
    private ItemRequest request;
}