package ru.practicum.shareit.user;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.user.dto.UserCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/users")
@AllArgsConstructor
@Validated
@Slf4j
public class UserController {

    private final UserClient userClient;

    @GetMapping
    public ResponseEntity<Object> getAllUsers() {
        log.info("Get all users");
        return userClient.getAllUsers();
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Object> getUserById(@Positive @PathVariable Long userId) {
        log.info("Get user with id = {}", userId);
        return userClient.getUserById(userId);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Object> updateUser(@Positive @PathVariable Long userId, @RequestBody UserCreateDto userDto) {
        log.info("Update user {}", userDto);
        return userClient.updateUser(userId, userDto);
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody @Valid UserCreateDto userDto) {
        log.info("Create user {}", userDto);
        return userClient.createUser(userDto);
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Object> deleteUser(@Positive @PathVariable Long userId) {
        return userClient.deleteUser(userId);
    }
}
