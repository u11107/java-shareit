package ru.practicum.shareit.item.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.storage.ItemStorage;

import javax.validation.Valid;
import java.util.List;
import java.util.Collections;

@Slf4j
@Service
@Validated
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public List<Item> getOwnerItems(Long userId) {
        return itemStorage.findOwnerItems(userId);
    }

    @Override
    public List<Item> searchItems(String string) {
        if (string.isBlank()) return Collections.emptyList();
        return itemStorage.searchItems(string);
    }

    @Override
    public Item getItemById(Long id) {
        return itemStorage.findItemById(id);
    }

    @Override
    public Item addItem(Long userId, @Valid Item item) {
        return itemStorage.addItem(userId, item);
    }

    @Override
    public Item updateItem(Long userId, Long id, Item newItem) {
        return itemStorage.updateItem(userId, id, newItem);
    }

    @Override
    public void deleteItemById(Long userId, Long id) {
        itemStorage.deleteItemById(userId, id);
    }

    public void deleteAllItems() {
        itemStorage.deleteAllItems();
    }
}
