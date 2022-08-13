package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final CommentRepository commentRepository;

    @Override
    public Item getItemById(Long itemId) {
        return itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
    }

    @Override
    public List<Item> getAllItemsByOwnerId(Long ownerId) {
        List<Item> itemList = itemRepository.findByOwnerId(ownerId);
        itemList.sort(Comparator.comparing(Item::getId));
        return itemList;
    }

    @Override
    public Item createItem(Long ownerId, Item item) {
        item.setOwner(userService.getUserById(ownerId));
        return itemRepository.save(item);
    }

    @Override
    public Item updateItem(Long ownerId, Item item) {
        Item updateItem = itemRepository.findById(item.getId())
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
        if (!updateItem.getOwner().getId().equals(ownerId)) {
            throw new NotFoundException("Редактировать вещь может только её владелец");
        }
        if (item.getName() != null && !item.getName().isBlank()) {
            updateItem.setName(item.getName());
        }
        if (item.getDescription() != null && !item.getDescription().isBlank()) {
            updateItem.setDescription(item.getDescription());
        }
        if (item.getAvailable() != null) {
            updateItem.setAvailable(item.getAvailable());
        }
        return itemRepository.save(updateItem);
    }

    @Override
    public List<Item> searchItemsByTextInNameAndDescription(String text) {
        if (text.isBlank()) {
            return Collections.emptyList();
        }
        return itemRepository.searchItemsByTextInNameAndDescription(text);
    }

    @Override
    public Comment createComment(Long userId, Long itemId, Comment comment) {
        Booking booking = bookingRepository.findCompletedBooking(userId, itemId, LocalDateTime.now());
        if (booking == null) {
            throw new ValidationException("Создание отзыва невозможно");
        }
        comment.setItem(itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id")));
        comment.setAuthor(userService.getUserById(userId));
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByItemId(Long itemId) {
        return commentRepository.findAllByItemId(itemId);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
