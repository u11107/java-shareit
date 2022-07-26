package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.ConflictException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.InMemoryItemStorageImpl;
import ru.practicum.shareit.user.storage.InMemoryUserStorageImpl;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final InMemoryItemStorageImpl itemStorage;
    private final InMemoryUserStorageImpl userStorage;

    @Override
    public ItemDto create(Long userId, ItemDto itemDto) {
        isUserDefined(userId);
        var item = ItemMapper.toItem(itemDto);
        item.setOwner(userStorage.get(userId));
        return ItemMapper.toItemDto(itemStorage.create(item));
    }

    @Override
    public ItemDto update(Long itemId, Long userId, ItemDto itemDto) {
        isUserDefined(userId);
        var item = itemStorage.get(itemId);
        if (item.getOwner().getId() != userId) {
            log.warn("Пользователь не владелец предмета");
            throw new ConflictException("Пользователь не владелец предмета");
        }
        if (itemDto.getName() != null) item.setName(itemDto.getName());
        if (itemDto.getDescription() != null) item.setDescription(itemDto.getDescription());
        if (itemDto.getAvailable() != null) item.setAvailable(itemDto.getAvailable());
        return ItemMapper.toItemDto(itemStorage.update(item));
    }

    @Override
    public ItemDto getItem(Long itemId) {
        return ItemMapper.toItemDto(itemStorage.get(itemId));
    }

    @Override
    public Collection<ItemDto> getAllForUser(Long userId) {
        isUserDefined(userId);
        return itemStorage.getAll().stream()
                .filter(i -> i.getOwner().getId() == userId)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<ItemDto> search(String text) {
        if (text.isEmpty()) {
            return Collections.emptyList();
        }
        String t = text.toLowerCase();
        return itemStorage.getAll().stream()
                .filter(i -> i.getDescription().toLowerCase().contains(t) ||
                        i.getName().toLowerCase().contains(t))
                .filter(Item::getAvailable)
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    private void isUserDefined(Long userId) {
        if (userId == null) {
            log.warn("Пользователь не определен");
            throw new ConflictException("Пользователь не определен");
        }
    }
}