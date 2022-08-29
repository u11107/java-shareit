package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemRequestServiceIntegrationTest {
    private final EntityManager entityManager;
    private final ItemRequestService itemRequestService;
    private final User user1 = new User(1L, "TestUser1", "test1@mail.com");
    private final User user2 = new User(2L, "TestUser2", "test2@mail.com");
    private final User newUser1 = new User(null, "TestUser1", "test1@mail.com");
    private final User newUser2 = new User(null, "TestUser2", "test2@mail.com");
    private final ItemRequest newItemRequest1 = new ItemRequest(null, "TestRequest1", user1, LocalDateTime.now());
    private final ItemRequest newItemRequest2 = new ItemRequest(null, "TestRequest2", user2, LocalDateTime.now());
    private final ItemRequest newItemRequest3 = new ItemRequest(null, "TestRequest3", user2, LocalDateTime.now());

    @Test
    void getItemRequestByIdTestOk() {
        entityManager.persist(newUser1);
        entityManager.persist(newItemRequest1);
        ItemRequest itemRequest = itemRequestService.getItemRequestById(1L, 1L);
        Assertions.assertNotNull(itemRequest);
        Assertions.assertEquals(1L, itemRequest.getId());
        Assertions.assertEquals("TestRequest1", itemRequest.getDescription());
        Assertions.assertEquals(user1, itemRequest.getRequester());
    }

    @Test
    void createItemRequestTestOk() {
        ItemRequest itemRequestForCreate = new ItemRequest(null, "Request", null, LocalDateTime.now());
        entityManager.persist(newUser1);
        itemRequestService.createItemRequest(1L, itemRequestForCreate);
        String query = "SELECT r FROM ItemRequest r WHERE r.id = 1";
        TypedQuery<ItemRequest> typedQuery = entityManager.createQuery(query, ItemRequest.class);
        ItemRequest itemRequestCreated = typedQuery.getSingleResult();
        Assertions.assertNotNull(itemRequestCreated);
        Assertions.assertEquals(1L, itemRequestCreated.getId());
        Assertions.assertEquals(itemRequestForCreate.getDescription(), itemRequestCreated.getDescription());
        Assertions.assertEquals(user1, itemRequestCreated.getRequester());
    }

    @Test
    void getAllRequestsByOwnerIdTestOk() {
        entityManager.persist(newUser1);
        entityManager.persist(newUser2);
        entityManager.persist(newItemRequest1);
        entityManager.persist(newItemRequest2);
        entityManager.persist(newItemRequest3);
        List<ItemRequest> itemRequestList = itemRequestService.getAllRequestsByOwnerId(2L);
        Assertions.assertNotNull(itemRequestList);
        Assertions.assertEquals(2, itemRequestList.size());
    }

    @Test
    void getAllRequestsByPageTestOk() {
        entityManager.persist(newUser1);
        entityManager.persist(newUser2);
        entityManager.persist(newItemRequest1);
        entityManager.persist(newItemRequest2);
        entityManager.persist(newItemRequest3);
        List<ItemRequest> itemRequestList = itemRequestService.getAllRequestsByPage(1L, 0, 10);
        Assertions.assertNotNull(itemRequestList);
        Assertions.assertEquals(2, itemRequestList.size());
    }
}
