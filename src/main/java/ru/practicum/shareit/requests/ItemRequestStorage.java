package ru.practicum.shareit.requests;

import java.util.Collection;

public interface ItemRequestStorage {
    ItemRequest get(Long id);

    Collection<ItemRequest> getAll();

    ItemRequest create(ItemRequest itemRequest);

    void remove(long id);

    ItemRequest update(ItemRequest itemRequest);
}
