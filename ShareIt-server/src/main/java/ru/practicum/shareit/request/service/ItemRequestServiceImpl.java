package ru.practicum.shareit.request.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ItemRequestServiceImpl implements ItemRequestService {

    private  final ItemRequestRepository itemRequestRepository;
    private final UserService userService;

    @Override
    public ItemRequest getItemRequestById(Long ownerId, Long itemRequestId) {
        userService.getUserById(ownerId);
        return itemRequestRepository.findById(itemRequestId)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
    }

    @Override
    public ItemRequest createItemRequest(Long ownerId, ItemRequest itemRequest) {
        itemRequest.setRequester(userService.getUserById(ownerId));
        return itemRequestRepository.save(itemRequest);
    }

    @Override
    public List<ItemRequest> getAllRequestsByOwnerId(Long ownerId) {
        userService.getUserById(ownerId);
        return itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(ownerId);
    }

    @Override
    public List<ItemRequest> getAllRequestsByPage(Long ownerId, Integer from, Integer size) {
        userService.getUserById(ownerId);
        return itemRequestRepository.findAllByIdNot(ownerId,
                PageRequest.of(from, size, Sort.by(Sort.Direction.DESC, "created")));
    }
}
