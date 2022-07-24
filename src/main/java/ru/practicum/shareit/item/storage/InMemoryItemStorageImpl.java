package ru.practicum.shareit.item.storage;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.ForbiddenAccessException;
import ru.practicum.shareit.exception.ItemNotFoundException;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.storage.UserStorage;

import javax.validation.ValidationException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Repository
public class InMemoryItemStorageImpl implements ItemStorage {

    private final Map<Long, Item> items = new HashMap<>();
    @Getter
    private final UserStorage inMemoryUserStorage;
    private static long id = 1;

    @Autowired
    public InMemoryItemStorageImpl(UserStorage inMemoryUserStorage) {
        this.inMemoryUserStorage = inMemoryUserStorage;
    }

    @Override
    public Item createItem(Long userId, Item item) {
        if (item.getId() != null) {
            log.error("Невозможно добавить вещь");
            throw new ValidationException();
        }
        if (!items.containsKey(item.getId())) {
            if (userId != null) {
                item.setOwner(inMemoryUserStorage.getById(userId));
            } else {
                throw new UserNotFoundException("Невозможно найти владельца " + userId);
            }
            item.setId(id++);
            items.put(item.getId(), item);
            log.info("Add item" + item);
            return item;
        } else {
            log.error("Пользователь уже существует");
            throw new ItemNotFoundException();
        }
    }

    @Override
    public Item updateItem(Long userId, Long itemId, Item newItem) {
        if (itemId == 0) {
            log.error("Невозможно обновить");
            throw new ValidationException();
        }
        Item item = items.get(itemId);
        if (item.getOwner().getId() != userId) {
            throw new ForbiddenAccessException("Обновление  возможно только для владельца");
        }
        if (newItem.getName() != null && !newItem.getName().isBlank()) {
            item.setName(newItem.getName());
        }
        if (newItem.getDescription() != null) {
            item.setDescription(newItem.getDescription());
        }
        if (newItem.getAvailable() != null) {
            item.setAvailable(newItem.getAvailable());
        }
        items.replace(item.getId(), item);
        log.info("Updated item " + item);
        return item;
    }

    @Override
    public void deleteItem(Item item) {
        if (item.getId() == 0) {
            log.error("Удаление невозможно");
            throw new ValidationException();
        }
        if (items.containsKey(item.getId())) {
            items.remove(item.getId());
        } else {
            throw new UserNotFoundException("Item " + " не найден");
        }
    }

    @Override
    public List<Item> findAllOwnerItems(long idOwner) {
        return items
                .values()
                .stream()
                .filter(item -> idOwner == (item.getOwner().getId())).collect(Collectors.toList());
    }

    @Override
    public Item getById(Long id) {
        if (!items.containsKey(id)) {
            throw new ItemNotFoundException();
        }
        return items.get(id);
    }

    @Override
    public Collection<Item> getAllItem() {
        return items.values();
    }
}
