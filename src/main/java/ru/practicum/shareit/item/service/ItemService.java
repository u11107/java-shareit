package ru.practicum.shareit.item.service;

import org.springframework.web.bind.annotation.PathVariable;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemService {

    ItemDto create(Long userId, ItemDto itemDto);

    ItemDto update(Long itemId, Long userId, ItemDto itemDto);

    ItemDto getItem(@PathVariable Long itemId);

    Collection<ItemDto> getAllForUser(Long userId);

    Collection<ItemDto> search(String text);
}
