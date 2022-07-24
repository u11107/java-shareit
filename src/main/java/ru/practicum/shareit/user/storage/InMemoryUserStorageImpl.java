package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserAlreadyExist;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.user.model.User;
import javax.validation.ValidationException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;

@Slf4j
@Repository
public class InMemoryUserStorageImpl implements UserStorage {
    private final HashSet<String> emails = new HashSet<>();
    private final HashMap<Long, User> users = new HashMap<>();
    private static long id = 1;

    @Override
    public User createUser(User user) {
        if (user.getId() != null) {
            log.error("Ошибка при создании пользователя");
            throw new ValidationException();
        }
        if (!users.containsKey(user.getId()) && !emails.contains(user.getEmail())) {
            user.setId(id++);
            emails.add(user.getEmail());
            users.put(user.getId(), user);
            log.info("Создан пользователь" + user);
            return user;
        } else {
            log.error("Пользователь уже существует");
            throw new UserAlreadyExist();
        }
    }

    @Override
    public User updateUser(Long id, User newUser) {
        if (id == 0) {
            log.error("Невозможно обновить пользователя: id не может быть 0");
            throw new ValidationException();
        }
        val userList = new ArrayList<>(users.values());
        User user = users.get(id);
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            for (User u : userList) {
                if (u.getEmail().equals(newUser.getEmail()) && (u.getId() != id)) {
                    log.error("Невозможно обновить пользователя: email должен быть уникальным");
                    throw new UserAlreadyExist();
                }
            }
            String oldEmail = user.getEmail();
            user.setEmail(newUser.getEmail());
            emails.remove(oldEmail);
            emails.add(newUser.getEmail());
        }
        if (newUser.getName() != null) {
            user.setName(newUser.getName());
        }
        users.replace(user.getId(), user);
        log.info("Пользователь обновлен " + user);
        return user;
    }

    @Override
    public void deleteUser(User user) {
        if (user.getId() != null) {
            log.error("Ошибка при создании пользователя");
            throw new ValidationException();
        }
        if(users.containsKey(user.getId())) {
            emails.remove(user.getEmail());
            users.remove(user.getId());
        } else {
            throw new UserNotFoundException("При удалении произошла ошибка, пользователь с таким " + id + " не найден");
        }
    }

    @Override
    public Collection<User> getAllUser() {
        return users.values();
    }

    @Override
    public User getById(Long id) {
        if(users.containsKey(id)) {
            throw  new UserNotFoundException("Пользователь с таким " +  id + " не найден");
        }
        return users.get(id);
    }
}
