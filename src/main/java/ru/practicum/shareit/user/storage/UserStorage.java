package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.model.User;

import java.util.Collection;

public interface UserStorage {

    User get(Long id);

    Collection<User> getAll();

    User create(User user);

    void remove(long id);

    User update(User user);
}
