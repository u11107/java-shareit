package ru.practicum.shareit.request.service;

import ru.practicum.shareit.request.model.ItemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequest getItemRequestById(Long ownerId, Long itemRequestId);

    ItemRequest createItemRequest(Long ownerId, ItemRequest itemRequest);

    List<ItemRequest> getAllRequestsByOwnerId(Long ownerId);

    List<ItemRequest> getAllRequestsByPage(Long ownerId, Integer from, Integer size);
}
