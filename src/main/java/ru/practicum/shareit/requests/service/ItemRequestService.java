package ru.practicum.shareit.requests.service;

import ru.practicum.shareit.requests.model.ItemRequest;

public interface ItemRequestService {

    ItemRequest getItemRequestById(Long id);

    ItemRequest createItemRequest(ItemRequest itemRequest);

    ItemRequest updateItemRequest(ItemRequest itemRequest);

    boolean deleteItemRequestById(Long id);
}
