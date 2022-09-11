package ru.practicum.shareit.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.dto.UserMapper;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.service.UserService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Validated
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @GetMapping
    public List<UserDto> getAllUsers() {
        return userService.getAllUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{userId}")
    public UserDto getUserById(@PathVariable long userId) {
        return userMapper.toUserDto(userService.getUserById(userId));
    }

    @PatchMapping("/{userId}")
    public UserDto updateUser(@PathVariable long userId,@RequestBody UserDto userDto) {
        userDto.setId(userId);
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userService.updateUser(userId, user));
    }

    @PostMapping
    public UserDto createUser(@RequestBody UserDto userDto) {
        User user = userMapper.toUser(userDto);
        return userMapper.toUserDto(userService.createUser(user));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable long userId) {
        userService.deleteUser(userId);
    }
}
