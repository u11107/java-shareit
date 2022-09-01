package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.repository.UserRepository;
import ru.practicum.shareit.user.service.UserService;
import ru.practicum.shareit.user.service.UserServiceImpl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserServiceTest {
    private UserService userService;
    @Mock
    private UserRepository userRepository;
    private final User user = new User(1L, "TestUser", "test@mail.com");
    private final User newUser = new User(null, "TestUser", "test@mail.com");

    @BeforeEach
    void beforeEach() {
        userService = new UserServiceImpl(userRepository);
    }

    @Test
    void getAllUsersTestOk() {
        Mockito.when(userRepository.findAll()).thenReturn(List.of(user));
        List<User> users = userService.getAllUsers();
        Assertions.assertNotNull(users);
        Assertions.assertEquals(1, users.size());
        Assertions.assertEquals(user, users.get(0));
    }

    @Test
    void getAllUsersWithNoUsers() {
        Mockito.when(userRepository.findAll()).thenReturn(Collections.emptyList());
        List<User> users = userService.getAllUsers();
        Assertions.assertNotNull(users);
        Assertions.assertEquals(0, users.size());
    }

    @Test
    void getUserByIdTestOk() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        User user1 = userService.getUserById(1);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(user, user1);
    }

    @Test
    void getUserByIdWithIncorrectId() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> userService.getUserById(1));
    }

    @Test
    void createUserTestOk() {
        Mockito.when(userRepository.save(newUser)).thenReturn(user);
        User user1 = userService.createUser(newUser);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(user, user1);
    }

    @Test
    void updateUserTestOk() {
        User toUpdateUser = new User(null, "UpdateName", "update@mail.com");
        User updatedUser = new User(1L, "UpdateName", "update@mail.com");
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        User user1 = userService.updateUser(1, toUpdateUser);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(updatedUser, user1);
    }

    @Test
    void updateUserTestWithNewName() {
        User toUpdateUser = new User(null, "UpdateName", null);
        User updatedUser = new User(1L, "UpdateName", "test@mail.com");
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        User user1 = userService.updateUser(1, toUpdateUser);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(updatedUser, user1);
    }

    @Test
    void updateUserTestWithNewEmail() {
        User toUpdateUser = new User(null, null, "update@mail.com");
        User updatedUser = new User(1L, "TestUser", "update@mail.com");
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(updatedUser)).thenReturn(updatedUser);
        User user1 = userService.updateUser(1, toUpdateUser);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(updatedUser, user1);
    }

    @Test
    void updateUserTestWithBlankNameAndEmail() {
        User toUpdateUser = new User(null, " ", " ");
        Mockito.when(userRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(user));
        Mockito.when(userRepository.save(user)).thenReturn(user);
        User user1 = userService.updateUser(1, toUpdateUser);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(user, user1);
    }

    @Test
    void updateUserWithIncorrectId() {
        User toUpdateUser = new User(null, "UpdateName", "update@mail.com");
        Assertions.assertThrows(NotFoundException.class, () -> userService.updateUser(100, toUpdateUser));
    }

    @Test
    void deleteUserByIdWithIncorrectId() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> userService.deleteUser(1L));
    }
}
