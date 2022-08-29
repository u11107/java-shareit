package ru.practicum.shareit.request.dto;

import lombok.*;
import ru.practicum.shareit.item.dto.ItemDto;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
public class ItemRequestDto {
    Long id;
    @NotBlank
    String description;
    Long requesterId;
    LocalDateTime created;
    List<ItemDto> items;
}
