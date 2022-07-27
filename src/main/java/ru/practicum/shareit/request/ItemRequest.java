package ru.practicum.shareit.request;

import lombok.Data;
import lombok.AllArgsConstructor;
import ru.practicum.shareit.user.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ItemRequest {
    private Long id;
    @NotNull
    @NotBlank
    private String description;
    private User requestor;
    @NotNull
    @FutureOrPresent
    private LocalDateTime created;
}
