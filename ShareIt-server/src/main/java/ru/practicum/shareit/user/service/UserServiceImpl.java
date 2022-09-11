package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public User getUserById(long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
    }

    @Override
    public User updateUser(long userId, User newUser) {
        User user = getUserById(userId);
        if (newUser.getName() != null && !newUser.getName().isBlank()) {
            user.setName(newUser.getName());
        }
        if (newUser.getEmail() != null && !newUser.getEmail().isBlank()) {
            user.setEmail(newUser.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public User createUser(User user) {
        if (user.getEmail() == null) {
            throw new ValidationException("Введен некорректный email");
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        getUserById(userId);
        userRepository.deleteById(userId);
    }
}
