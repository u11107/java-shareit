package ru.practicum.shareit.request;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestRepositoryTest {
    @Autowired
    private TestEntityManager manager;
    @Autowired
    private ItemRequestRepository repository;
    private final User user1 = new User(1L, "TestUser1", "test1@mail.com");
    private final User user2 = new User(2L, "TestUser2", "test2@mail.com");
    private final User newUser1 = new User(null, "TestUser1", "test1@mail.com");
    private final User newUser2 = new User(null, "TestUser2", "test2@mail.com");
    private final ItemRequest newItemRequest1 = new ItemRequest(null, "TestRequest1", user1, LocalDateTime.MIN);
    private final ItemRequest newItemRequest2 = new ItemRequest(null, "TestRequest2", user2, LocalDateTime.MIN);
    private final ItemRequest newItemRequest3 = new ItemRequest(null, "TestRequest3", user2, LocalDateTime.MIN);
    private final ItemRequest itemRequest1 = new ItemRequest(1L, "TestRequest1", user1, LocalDateTime.MIN);
    private final ItemRequest itemRequest2 = new ItemRequest(2L, "TestRequest2", user2, LocalDateTime.MIN);
    private final ItemRequest itemRequest3 = new ItemRequest(3L, "TestRequest3", user2, LocalDateTime.MIN);

    @Test
    void findAllByIdNotTestOk() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItemRequest1);
        manager.persist(newItemRequest2);
        manager.persist(newItemRequest3);
        List<ItemRequest> itemRequestList = repository.findAllByIdNot(1L, PageRequest.of(0, 10));
        Assertions.assertNotNull(itemRequestList);
        Assertions.assertEquals(2, itemRequestList.size());
        Assertions.assertEquals(itemRequest2, itemRequestList.get(0));
        Assertions.assertEquals(itemRequest3, itemRequestList.get(1));
        Assertions.assertEquals(user2, itemRequestList.get(0).getRequester());
    }

    @Test
    void findAllByRequesterIdOrderByCreatedDescTestOk() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItemRequest1);
        manager.persist(newItemRequest2);
        manager.persist(newItemRequest3);
        List<ItemRequest> itemRequestList = repository.findAllByRequesterIdOrderByCreatedDesc(2L);
        Assertions.assertNotNull(itemRequestList);
        Assertions.assertEquals(2, itemRequestList.size());
        Assertions.assertEquals(itemRequest2, itemRequestList.get(0));
        Assertions.assertEquals(itemRequest3, itemRequestList.get(1));
        Assertions.assertEquals(user2, itemRequestList.get(0).getRequester());
    }
}
