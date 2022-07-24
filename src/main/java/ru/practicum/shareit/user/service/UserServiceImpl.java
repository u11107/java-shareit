package ru.practicum.shareit.user.service;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.storage.UserStorage;
import java.util.Collection;

@Slf4j
@Service
@Validated
public class UserServiceImpl implements UserService {

    @Getter
    private final UserStorage userStorage;

    @Autowired
    public UserServiceImpl(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    @Override
    public User createUser(User user) {
      return userStorage.createUser(user);
    }

    @Override
    public User updateUser(Long id, User newUser) {
        return userStorage.updateUser(id, newUser);
    }

    @Override
    public void deleteUser(User user) {
        userStorage.deleteUser(user);
    }

    @Override
    public Collection<User> getAllUser() {
        return userStorage.getAllUser();
    }

    @Override
    public User getById(Long id) {
        return userStorage.getById(id);
    }
}
