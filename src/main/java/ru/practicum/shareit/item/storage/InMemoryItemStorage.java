package ru.practicum.shareit.item.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.InMemoryUserStorage;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.AccessDeniedException;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryItemStorage implements ItemStorage {
    private final InMemoryUserStorage userStorage;
    private final Map<Long, Item> items = new HashMap<>();
    private long nextItemId = 0;

    @Autowired
    public InMemoryItemStorage(InMemoryUserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public List<Item> findOwnerItems(Long userId) {
        return items.values()
                .stream()
                .filter(item -> item.getOwner().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Item> searchItems(String string) {
        return items.values()
                .stream()
                .filter(
                        item -> item.getAvailable() &&
                        (item.getName().toLowerCase().contains(string.toLowerCase()) ||
                        item.getDescription().toLowerCase().contains(string.toLowerCase()))
                )
                .collect(Collectors.toList());
    }

    @Override
    public Item findItemById(Long id) {
        if (!items.containsKey(id)) {
            throw new ItemNotFoundException("There is no item with id");
        }
        return items.get(id);
    }

    @Override
    public Item addItem(Long userId, Item item) {
        item.setOwner(userStorage.findUserById(userId));
        item.setId(getItemId());
        items.put(item.getId(), item);
        log.info("InMemoryItemStorage.addItem: item {} " +
                 "successfully added to storage", item.getId());
        return item;
    }

    @Override
    public Item updateItem(Long userId, Long id, Item newItem) {
        var item = findItemById(id);
        if (!userId.equals(item.getOwner().getId())) {
            throw new AccessDeniedException("User is not allowed to change item");
        }
        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null && !newItem.getDescription().isBlank()) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) item.setAvailable(newItem.getAvailable());
        return item;
    }

    @Override
    public void deleteItemById(Long userId, Long id) {
        var item = findItemById(id);
        if (!userId.equals(item.getOwner().getId())) {
            throw new AccessDeniedException("User is not allowed to delete item");
        }
        items.remove(id);
    }

    @Override
    public void deleteAllItems() {
        items.clear();
        nextItemId = 0;
    }

    private Long getItemId() {
        nextItemId += 1;
        return nextItemId;
    }
}
