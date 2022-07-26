package ru.practicum.shareit.item.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.validator.ItemValidator;
import java.util.*;

@Component("inMemoryItemStorageImpl")
@Slf4j
@RequiredArgsConstructor
public class InMemoryItemStorageImpl implements ItemStorage {

    private final HashMap<Long, Item> items = new HashMap<>();
    private long id = 1;
    private final ItemValidator validator;

    @Override
    public Item get(Long id) {
        if (!items.containsKey(id)) {
            log.warn("Предмет c таким id не найден.");
            throw new NotFoundException("Предмет c таким id не найден.");
        }
        return items.get(id);
    }

    @Override
    public Collection<Item> getAll() {
        return items.values();
    }

    @Override
    public Item create(Item item) {
        validator.validateItem(item);
        item.setId(id++);
        items.put(item.getId(), item);
        log.debug("Текущее количество предметов: {}", items.size());
        return item;
    }

    @Override
    public void remove(Item item) {
        if (!items.containsKey(id)) {
            log.warn("Предмет c таким id не найден.");
            throw new NotFoundException("Предмет c таким id не найден.");
        }
        items.remove(item.getId());
    }

    @Override
    public Item update(Item item) {
        validator.validateItem(item);
        items.put(item.getId(), item);
        log.debug("Текущее количество предметов: {}", items.size());
        return item;
    }
}
