package ru.practicum.shareit.item.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;

import java.util.Collection;
import java.util.List;

@Slf4j
@Service
@Validated
public class ItemServiceImpl implements ItemService {
    @Getter
    private final ItemStorage itemStorage;

    @Autowired
    public ItemServiceImpl(ItemStorage itemStorage) {
        this.itemStorage = itemStorage;
    }

    @Override
    public Item createItem(Long userId, Item item) {
        return itemStorage.createItem(userId, item);
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item newItem) {
        return itemStorage.updateItem(userId, itemId, newItem);
    }

    @Override
    public void deleteItem(Item item) {
        itemStorage.deleteItem(item);
    }

    @Override
    public List<Item> findAllOwnerItems(long idOwner) {
        return itemStorage.findAllOwnerItems(idOwner);
    }

    @Override
    public Item getById(Long id) {
        return itemStorage.getById(id);
    }

    @Override
    public Collection<Item> getAllItem() {
        return itemStorage.getAllItem();
    }
}
