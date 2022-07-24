package ru.practicum.shareit.requests.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.practicum.shareit.user.model.User;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
public class ItemRequest {
    private long id;
    @Size(max=200)
    private String description;
    @NotNull
    private User requestor;
    private LocalDateTime created;
}
