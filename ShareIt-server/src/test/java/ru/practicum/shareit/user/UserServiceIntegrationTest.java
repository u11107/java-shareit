package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class UserServiceIntegrationTest {
    private final EntityManager entityManager;
    private final UserService userService;
    private final User user = new User(1L, "TestUser", "test@mail.com");
    private final User newUser = new User(null, "TestUser", "test@mail.com");

    @Test
    void getAllUsersTestOk() {
        User newUser1 = new User(null, "TestUser1", "test1@mail.com");
        User newUser2 = new User(null, "TestUser2", "test2@mail.com");
        User newUser3 = new User(null, "TestUser3", "test3@mail.com");
        User createdUser1 = new User(1L, "TestUser1", "test1@mail.com");
        User createdUser2 = new User(2L, "TestUser2", "test2@mail.com");
        User createdUser3 = new User(3L, "TestUser3", "test3@mail.com");
        entityManager.persist(newUser1);
        entityManager.persist(newUser2);
        entityManager.persist(newUser3);
        List<User> createdUsers = List.of(createdUser1, createdUser2, createdUser3);
        List<User> userList = userService.getAllUsers();
        Assertions.assertNotNull(userList);
        Assertions.assertEquals(3, userList.size());
        Assertions.assertEquals(createdUsers.get(0), userList.get(0));
        Assertions.assertEquals(createdUsers.get(1), userList.get(1));
        Assertions.assertEquals(createdUsers.get(2), userList.get(2));
    }

    @Test
    void getUserByIdTestOk() {
        entityManager.persist(newUser);
        User user1 = userService.getUserById(1);
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(user, user1);
    }

    @Test
    void createUserTestOk() {
        userService.createUser(newUser);
        String query = "SELECT u FROM User u WHERE u.id = 1";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        User user1 = typedQuery.getSingleResult();
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(user, user1);
    }

    @Test
    void updateUserTestOk() {
        entityManager.persist(newUser);
        User toUpdateUser = new User(null, "UpdateName", "update@mail.com");
        userService.updateUser(1, toUpdateUser);
        String query = "SELECT u FROM User u WHERE u.id = 1";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        User user1 = typedQuery.getSingleResult();
        Assertions.assertNotNull(user1);
        Assertions.assertEquals(1L, user1.getId());
        Assertions.assertEquals(toUpdateUser.getName(), user1.getName());
        Assertions.assertEquals(toUpdateUser.getEmail(), user1.getEmail());
    }

    @Test
    void deleteUserById() {
        entityManager.persist(newUser);
        userService.deleteUser(1L);
        String query = "SELECT u FROM User u WHERE u.id = 1";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        Assertions.assertThrows(NoResultException.class, () -> typedQuery.getSingleResult());
    }
}
