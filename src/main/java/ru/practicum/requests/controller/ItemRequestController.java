package ru.practicum.requests.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.requests.RequestMapper;
import ru.practicum.requests.dto.ItemRequestDto;
import ru.practicum.requests.storage.ItemRequestRepository;
import ru.practicum.user.storage.UserRepository;

@RestController
@RequestMapping(path = "/requests")
public class ItemRequestController {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;

    public ItemRequestController(ItemRequestRepository itemRequestRepository, UserRepository userRepository) {
        this.itemRequestRepository = itemRequestRepository;
        this.userRepository = userRepository;
    }

    @PostMapping
    public ItemRequestDto addRequest(@RequestBody ItemRequestDto dto) {
        var s = itemRequestRepository.saveAndFlush(RequestMapper.toRequest(dto, userRepository.getReferenceById(dto.getRequesterId())));
        return RequestMapper.toDto(s);
    }
}
