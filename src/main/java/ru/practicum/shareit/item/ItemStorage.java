package ru.practicum.shareit.item;

import java.util.List;

public interface ItemStorage {
    List<Item> searchOwnerItem(Long userId);

    List<Item> searchItems(String text);

    Item searchItemById(Long id);

    Item addItem(Long userId, Item item);

    Item updateItem(Long userId, Long id, Item newItem);

    void deleteItemById(Long userId, Long id);

    void deleteAllItems();
}
