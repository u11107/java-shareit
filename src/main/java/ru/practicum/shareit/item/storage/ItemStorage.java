package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;
import java.util.Collection;

public interface ItemStorage {

    Item get(Long id);

    Collection<Item> getAll();

    Item create(Item item);

    void remove(Item item);

    Item update(Item item);
}
