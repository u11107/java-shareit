package ru.practicum.shareit.item.dto;

import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.requests.ItemRequestStorage;
import ru.practicum.shareit.user.storage.UserStorage;

@RequiredArgsConstructor
public class ItemMapper {

    private static UserStorage userStorage;
    private static ItemRequestStorage itemRequestStorage;

    public static ItemDto toItemDto(Item item) {
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getRequest() != null ? item.getRequest().getId() : null
        );
    }

    public static Item toItem(ItemDto itemDto) {
        return new Item(
                itemDto.getId(),
                itemDto.getName(),
                itemDto.getDescription(),
                itemDto.getAvailable(),
                null,
                itemDto.getRequestId() != null ? itemRequestStorage.get(itemDto.getRequestId()) : null
        );
    }
}