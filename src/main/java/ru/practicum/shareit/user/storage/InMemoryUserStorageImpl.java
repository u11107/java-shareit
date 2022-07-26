package ru.practicum.shareit.user.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.validator.UserValidator;

import javax.validation.ValidationException;
import java.util.*;

@Component("inMemoryUserStorageImpl")
@Slf4j
@RequiredArgsConstructor
public class InMemoryUserStorageImpl implements UserStorage {

    private final HashMap<Long, User> users = new HashMap<>();
    private long id = 1;

    private final UserValidator validator;

    @Override
    public Collection<User> getAll() {
        return users.values();
    }

    @Override
    public User get(Long id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь c таким id не найден.");
            throw new ValidationException("Пользователь c таким id не найден.");
        }
        return users.get(id);
    }

    @Override
    public User create(User user) {
        validator.validateUser(user);
        isEmailExists(user.getEmail());
        user.setId(id++);
        users.put(user.getId(), user);
        log.debug("Текущее количество пользователей: {}", users.size());
        return user;
    }

    @Override
    public void remove(long id) {
        if (!users.containsKey(id)) {
            log.warn("Пользователь не найден.");
            throw new NotFoundException("Пользователь не найден.");
        }
        users.remove(id);
    }

    @Override
    public User update(User user) {
        validator.validateUser(user);
        users.put(user.getId(), user);
        log.debug("Текущее количество пользователей: {}", users.size());
        return user;
    }

    public void isEmailExists(String email) {
        for (User u : users.values()) {
            if (u.getEmail().equals(email)) {
                log.warn("Пользователь с таким e-mail уже существует");
                throw new ValidationException("Пользователь с таким e-mail уже существует");
            }
        }
    }
}

