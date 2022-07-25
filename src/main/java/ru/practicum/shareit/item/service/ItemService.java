package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;

import javax.validation.Valid;
import java.util.List;

public interface ItemService {
    List<Item> getOwnerItems(Long userId);

    Item addItem(Long userId, @Valid Item item);

    Item updateItem(Long userId, Long id, Item newItem);

    void deleteItemById(Long userId, Long id);

    Item getItemById(Long id);

    List<Item> searchItems(String string);
}
