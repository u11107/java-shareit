package ru.practicum.shareit.item.service;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemService {

    Item getItemById(Long itemId);

    List<Item> getAllItemsByOwnerId(Long ownerId, Integer from, Integer size);

    Item createItem(Long ownerId, Item item);

    Item updateItem(Long ownerId, Item item);

    List<Item> searchItemsByTextInNameAndDescription(String text, Integer from, Integer size);

    void deleteItemById(Long ownerId, Long itemId);

    Comment createComment(Long userId, Long itemId, Comment comment);

    List<Comment> getCommentsByItemId(Long itemId);

    List<Item> getItemsByRequestId(Long requestId);
}
