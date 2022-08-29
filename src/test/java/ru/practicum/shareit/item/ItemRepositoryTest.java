package ru.practicum.shareit.item;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRepositoryTest {
    @Autowired
    private TestEntityManager manager;
    @Autowired
    private ItemRepository repository;
    private final User user1 = new User(1L, "TestUser1", "test1@mail.com");
    private final User user2 = new User(2L, "TestUser2", "test2@mail.com");
    private final User newUser1 = new User(null, "TestUser1", "test1@mail.com");
    private final User newUser2 = new User(null, "TestUser2", "test2@mail.com");
    private final ItemRequest newItemRequest1 = new ItemRequest(null, "TestRequest1", user1, LocalDateTime.MIN);
    private final ItemRequest itemRequest1 = new ItemRequest(1L, "TestRequest1", user1, LocalDateTime.MIN);
    private final Item newItem1 = new Item(null, "TestItem1", "TestDescription1", true, user1, itemRequest1);
    private final Item newItem2 = new Item(null, "TestItem2", "TestDescription2", true, user2, null);
    private final Item item = new Item(1L, "TestItem1", "TestDescription1", true, user1, itemRequest1);

    @Test
    void findByOwnerIdTestOk() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItemRequest1);
        manager.persist(newItem1);
        manager.persist(newItem2);
        List<Item> items = repository.findByOwnerId(1L, PageRequest.of(0, 10));
        Assertions.assertNotNull(items);
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(item, items.get(0));
    }

    @Test
    void searchItemsByTextInNameAndDescription_WithLowAndUpperCase() {
        manager.persist(newUser1);
        manager.persist(newItemRequest1);
        manager.persist(newItem1);
        List<Item> items = repository.searchItemsByTextInNameAndDescription("iTeM", PageRequest.of(0, 10));
        Assertions.assertNotNull(items);
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(item, items.get(0));
    }

    @Test
    void findItemsByRequestIdTestOk() {
        manager.persist(newUser1);
        manager.persist(newItemRequest1);
        manager.persist(newItem1);
        List<Item> items = repository.findItemsByRequestId(1L);
        Assertions.assertNotNull(items);
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(item, items.get(0));
    }
}
