package ru.practicum.item.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDto {
    private int id;
    private String name;
    private String description;
    private Boolean available;
    private int ownerId;
    private ItemBookingDto lastBooking;
    private ItemBookingDto nextBooking;
    private Collection<CommentDto> comments;
}
