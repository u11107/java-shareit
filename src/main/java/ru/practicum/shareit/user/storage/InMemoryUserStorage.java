package ru.practicum.shareit.user.storage;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.practicum.shareit.exception.UserNotFoundException;
import ru.practicum.shareit.exception.EmailAlreadyTakenException;
import ru.practicum.shareit.user.model.User;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

@Slf4j
@Repository
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private long nextUserId = 0;

    @Override
    public List<User> findAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User findUserById(Long id) {
        if (!users.containsKey(id)) {
            throw new UserNotFoundException("There is no user with id %d");
        }
        return users.get(id);
    }

    @Override
    public User addUser(User user) {
        checkEmailAvailability(user.getEmail());
        user.setId(getUserId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(Long id, User newUser) {
        var user = findUserById(id);
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            if (!user.getEmail().equals(newUser.getEmail())) {
                checkEmailAvailability(newUser.getEmail());
            }
            user.setEmail(newUser.getEmail());
        }
        return user;
    }

    @Override
    public void deleteUserById(Long id) {
        findUserById(id);
        users.remove(id);
    }

    @Override
    public void deleteAllUsers() {
        users.clear();
        nextUserId = 0;
    }

    private void checkEmailAvailability(String email) {
        for (var user : users.values()) {
            if (user.getEmail().equals(email)) {
                throw new EmailAlreadyTakenException("Email is already taken");
            }
        }
    }

    private Long getUserId() {
        nextUserId += 1;
        return nextUserId;
    }
}
