package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;

public interface ItemStorage {

    Item createItem(Long userId, Item item);

    Item updateItem(Long userId, Long id, Item newItem);

    void deleteItem(Item item);

    List<Item> findAllOwnerItems(long idOwner);

    Item getById(Long id);

    Collection<Item> getAllItem();
}
