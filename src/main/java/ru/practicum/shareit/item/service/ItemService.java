package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.Collection;
import java.util.List;

public interface ItemService {
    Item createItem(Long userId, Item item);

    Item updateItem(Long userId, Long id, Item newItem);

    void deleteItem(Item item);

    List<Item> findAllOwnerItems(long idOwner);

    Item getById(Long id);

    Collection<Item> getAllItem();
}
