package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.List;

public interface UserStorage {
    List<User> findAllUsers();

    User findUserById(Long id);

    User addUser(User user);

    User updateUser(Long id, User newUser);

    void deleteUserById(Long id);

    void deleteAllUsers();
}
