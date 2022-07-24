package ru.practicum.shareit.user.service;

import ru.practicum.shareit.user.model.User;
import java.util.Collection;

public interface UserService {
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(User user);
    Collection<User> getAllUser();
    User getById(Long id);
}
