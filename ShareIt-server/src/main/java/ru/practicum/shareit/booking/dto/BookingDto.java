package ru.practicum.shareit.booking.dto;

import lombok.Setter;
import lombok.Value;
import lombok.experimental.NonFinal;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;

@Value
public class BookingDto {

    Long id;
    LocalDateTime start;
    LocalDateTime end;
    Item item;
    User booker;
    @NonFinal
    @Setter
    BookingStatus status;
}
