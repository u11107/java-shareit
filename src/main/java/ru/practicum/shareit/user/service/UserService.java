package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;

import javax.validation.Valid;
import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long id);

    User addUser(@Valid User user);

    User updateUser(Long id, User newUser);

    void deleteUserById(Long id);

    void deleteAllUsers();
}
