package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;
import java.util.Collection;

public interface UserStorage {
    User createUser(User user);
    User updateUser(Long id, User user);
    void deleteUser(User user);
    Collection<User> getAllUser();
    User getById(Long id);
}
