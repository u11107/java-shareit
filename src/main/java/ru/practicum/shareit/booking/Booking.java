package ru.practicum.shareit.booking;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Booking {
    private Long id;
    @NotNull
    @FutureOrPresent
    private LocalDate start;
    @NotNull
    @Future
    private LocalDate end;
    private Item item;
    private User booker;
    @NotNull
    private String status;
}
