package ru.practicum.user.service;

import org.springframework.stereotype.Component;
import ru.practicum.user.UserMapper;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;
import ru.practicum.user.storage.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getById(int userId) {
        return userRepository.getReferenceById(userId);
    }

    public Collection<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserMapper::toUserDto).collect(Collectors.toList());
    }

    public boolean existsById(int id) {
        return userRepository.existsById(id);
    }

    public User addNew(User model) {
        return userRepository.saveAndFlush(model);
    }

    public User save(User model) {
        return userRepository.saveAndFlush(model);
    }

    public void deleteById(int id) {
        userRepository.deleteById(id);
    }
}
