package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    User getUserById(long userId);

    User updateUser(long userId, User user);

    User createUser(User user);

    void deleteUser(Long userId);
}
