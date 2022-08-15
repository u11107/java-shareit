package ru.practicum.shareit.item.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.dto.CommentDto;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/items")
@AllArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final BookingService bookingService;

    @GetMapping("/{itemId}")
    public ItemDto getItemById(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                               @PathVariable long itemId) {
        User user = new User();
        Item item = itemService.getItemById(itemId);
        ItemDto itemDto = ItemMapper.toItemDto(item);
        addCommentsToItemDto(itemDto);
        if (item.getOwner().getId().equals(ownerId)) {
            addLastBookingAndNextBookingToItemDto(itemDto);
        }
        return itemDto;
    }

    @GetMapping
    public List<ItemDto> getAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        List<ItemDto> itemDtoList = itemService.getAllItemsByOwnerId(ownerId)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
        itemDtoList.forEach(this::addLastBookingAndNextBookingToItemDto);
        itemDtoList.forEach(this::addCommentsToItemDto);
        return itemDtoList;
    }

    @PostMapping
    public ItemDto createItem(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @RequestBody @Valid ItemDto itemDto) {
        Item item = ItemMapper.toItemNew(itemDto);
        item = itemService.createItem(ownerId, item);
        return ItemMapper.toItemDto(item);
    }

    @PatchMapping("/{itemId}")
    public ItemDto updateItem(@PathVariable long itemId,
                              @RequestHeader("X-Sharer-User-Id") Long ownerId,
                              @RequestBody ItemDto itemDto) {
        itemDto.setId(itemId);
        Item item = ItemMapper.toItem(itemDto);
        item = itemService.updateItem(ownerId, item);
        return ItemMapper.toItemDto(item);
    }


    @GetMapping("/search")
    public List<ItemDto> searchItemsByTextInNameAndDescription(@RequestParam("text") String text) {
        return itemService.searchItemsByTextInNameAndDescription(text)
                .stream()
                .map(ItemMapper::toItemDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto createComment(@RequestHeader("X-Sharer-User-Id") Long userId,
                                    @PathVariable Long itemId,
                                    @RequestBody @Valid CommentDto commentDto) {
        Comment comment = CommentMapper.toComment(commentDto);
        return CommentMapper.toCommentDto(itemService.createComment(userId, itemId, comment));
    }

    private void addLastBookingAndNextBookingToItemDto(ItemDto itemDto) {
        if (bookingService.findLastBooking(itemDto.getId()).isPresent()) {
            itemDto.setLastBooking(bookingService.findLastBooking(itemDto.getId()).get());
        }
        if (bookingService.findNextBooking(itemDto.getId()).isPresent()) {
            itemDto.setNextBooking(bookingService.findNextBooking(itemDto.getId()).get());
        }
    }

    public void addCommentsToItemDto(ItemDto itemDto) {
        itemDto.setComments(itemService.getCommentsByItemId(itemDto.getId()).stream()
                .map(CommentMapper::toCommentDto)
                .collect(Collectors.toList()));
    }
}
