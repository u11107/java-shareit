package ru.practicum.shareit.item.dto;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class CommentDto {
    Long id;
    String text;
    String authorName;
    LocalDateTime created;
}
