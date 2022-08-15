package ru.practicum.shareit.requests.service;

import org.springframework.stereotype.Service;
import ru.practicum.shareit.requests.model.ItemRequest;

@Service
public class ItemRequestServiceImpl implements ItemRequestService {

    @Override
    public ItemRequest getItemRequestById(Long id) {
        return null;
    }

    @Override
    public ItemRequest createItemRequest(ItemRequest itemRequest) {
        return null;
    }

    @Override
    public ItemRequest updateItemRequest(ItemRequest itemRequest) {
        return null;
    }

    @Override
    public boolean deleteItemRequestById(Long id) {
        return false;
    }
}
