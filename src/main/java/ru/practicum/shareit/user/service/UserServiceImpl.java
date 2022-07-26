package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.user.storage.InMemoryUserStorageImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.dto.UserMapper;
import java.util.Collection;
import java.util.stream.Collectors;


@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final InMemoryUserStorageImpl userStorage;

    @Override
    public UserDto create(UserDto userDto) {
        User user = UserMapper.toUser(userDto);
        return UserMapper.toUserDto(userStorage.create(user));
    }

    @Override
    public UserDto update(Long id, UserDto userDto) {
        var user = userStorage.get(id);
        if (userDto.getName() != null) user.setName(userDto.getName());
        if (userDto.getEmail() != null) {
            userStorage.isEmailExists(userDto.getEmail());
            user.setEmail(userDto.getEmail());
        }
        return UserMapper.toUserDto(userStorage.update(user));
    }

    @Override
    public Collection<UserDto> getAll() {
        return userStorage.getAll().stream()
                .map(UserMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Override
    public UserDto getUser(Long id) {
        return UserMapper.toUserDto(userStorage.get(id));
    }
}
