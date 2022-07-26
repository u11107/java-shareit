package ru.practicum.shareit.item;

import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.model.ItemForUpdate;

import java.util.Collection;
import java.util.List;

public interface ItemStorage {
    Item add(Long userId, Item item);

    Item update(long userId, long itemId, ItemForUpdate item);

    void delete(Item item);

    List<Item> findAllOwnerItems(long idOwner);

    Item findById(Long id);

    Collection<Item> findAllItems();
}