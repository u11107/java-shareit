package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;


public interface UserService {

    UserDto create(UserDto userDto);

    UserDto update(Long id, UserDto userDto);

    Collection<UserDto> getAll();

    UserDto getUser(Long id);
}
