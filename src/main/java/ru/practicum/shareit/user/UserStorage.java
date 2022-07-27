package ru.practicum.shareit.user;

import java.util.List;

public interface UserStorage {
    List<User> getAllUser();

    User searchUserById(Long id);

    User addUser(User user);

    User updateUser(Long id, User newUser);

    void deleteUserById(Long id);

    void deleteAllUsers();
}
