package ru.practicum.shareit.request.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
public class ItemRequestController {

    private final ItemRequestService itemRequestService;
    private final ItemService itemService;

    @GetMapping
    public List<ItemRequestDto> getAllRequestsByOwnerId(@RequestHeader("X-Sharer-User-Id") Long ownerId) {
        List<ItemRequestDto> requestList = itemRequestService.getAllRequestsByOwnerId(ownerId)
                .stream().map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
        requestList.forEach(request -> request.setItems(itemService.getItemsByRequestId(request.getId())
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList())));
        return requestList;
    }

    @PostMapping
    public ItemRequestDto createItemRequest(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                            @RequestBody ItemRequestDto itemRequestDto) {
        ItemRequest itemRequest = ItemRequestMapper.toItemRequest(itemRequestDto);
        return ItemRequestMapper.toItemRequestDto(itemRequestService.createItemRequest(ownerId, itemRequest));
    }

    @GetMapping("/all")
    public List<ItemRequestDto> getAllRequestsByPage(
                                            @RequestHeader("X-Sharer-User-Id") Long ownerId,
                                            @RequestParam(defaultValue = "0") Integer from,
                                            @RequestParam(defaultValue = "10") Integer size) {
        List<ItemRequestDto> requestDtos = itemRequestService.getAllRequestsByPage(ownerId, from, size).stream()
                .map(ItemRequestMapper::toItemRequestDto).collect(Collectors.toList());
        requestDtos.forEach(request -> request.setItems(itemService.getItemsByRequestId(request.getId())
                .stream().map(ItemMapper::toItemDto).collect(Collectors.toList())));
        return requestDtos;
    }

    @GetMapping("/{requestId}")
    public ItemRequestDto getItemRequestById(@RequestHeader("X-Sharer-User-Id") Long ownerId,
                                             @PathVariable Long requestId) {
        ItemRequest itemRequest = itemRequestService.getItemRequestById(ownerId, requestId);
        ItemRequestDto itemRequestDto = ItemRequestMapper.toItemRequestDto(itemRequest);
        itemRequestDto.setItems(itemService.getItemsByRequestId(requestId).stream()
                .map(ItemMapper::toItemDto).collect(Collectors.toList()));
        return itemRequestDto;
    }
}
