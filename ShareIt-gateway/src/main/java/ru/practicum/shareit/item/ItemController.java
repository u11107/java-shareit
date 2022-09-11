package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.CommentCreateDto;
import ru.practicum.shareit.item.dto.ItemCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
@Validated
@Slf4j
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                      @PathVariable @Positive Long itemId) {
        log.info("Get item with id = {} by user id = {}", itemId, ownerId);
        return itemClient.getItemById(ownerId, itemId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                              @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                              @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.info("Get all items by owner with id = {} and page from = {}, size = {}", ownerId, from, size);
        return itemClient.getAllItemsByOwnerId(ownerId, from, size);
    }

    @PostMapping
    public ResponseEntity<Object> createItem(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                              @RequestBody @Valid ItemCreateDto itemCreateDto) {
        log.info("Create item = {} by user id = {}", itemCreateDto, ownerId);
        return itemClient.createItem(ownerId, itemCreateDto);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable @Positive Long itemId,
                              @RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                              @RequestBody ItemCreateDto itemCreateDto) {
        log.info("Update item with id = {} by user id = {}", itemId, ownerId);
        return itemClient.updateItem(ownerId, itemCreateDto, itemId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItemsByTextInNameAndDescription(
                                                        @RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                                        @RequestParam(name = "text", required = false) String text,
                                                        @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                        @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.info("Search items by text = {} and page from = {}, size = {}", text, from, size);
        return itemClient.searchItemsByTextInNameAndDescription(ownerId, text, from, size);
    }

    @DeleteMapping("/{itemId}")
    public ResponseEntity<Object> deleteItemById(@PathVariable @Positive Long itemId,
                                                 @RequestHeader("X-Sharer-User-Id") @Positive Long ownerId) {
        log.info("Delete item with id = {} by user id = {}", itemId, ownerId);
        return itemClient.deleteItemById(ownerId, itemId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> createComment(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                    @PathVariable @Positive Long itemId,
                                    @RequestBody @Valid CommentCreateDto commentCreateDto) {
        log.info("Create comment = {} for item with id = {} by user id = {}", commentCreateDto, itemId, userId);
        return itemClient.createComment(userId, commentCreateDto, itemId);
    }
}
