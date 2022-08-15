package ru.practicum.shareit.requests.dto;

import lombok.Value;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Value
public class ItemRequestDto {
    Long id;
    String description;
    User requester;
    LocalDateTime created;
}
