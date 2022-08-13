package ru.practicum.requests;

import ru.practicum.requests.dto.ItemRequestDto;
import ru.practicum.requests.model.ItemRequest;
import ru.practicum.user.model.User;

public class RequestMapper {

    public static ItemRequest toRequest(ItemRequestDto dto, User requester) {
        return new ItemRequest(dto.getId(), dto.getDescription(), requester, dto.getCreated());
    }

    public static ItemRequestDto toDto(ItemRequest entity) {
        return new ItemRequestDto(entity.getId(), entity.getDescription(), entity.getRequester().getId(), entity.getCreated());
    }
}
