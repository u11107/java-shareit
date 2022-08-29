package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.user.model.User;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ItemServiceIntegrationTest {
    private final EntityManager manager;
    private final ItemService itemService;
    private final User user1 = new User(1L, "TestUser1", "test1@mail.com");
    private final User user2 = new User(2L, "TestUser2", "test2@mail.com");
    private final User newUser1 = new User(null, "TestUser1", "test1@mail.com");
    private final User newUser2 = new User(null, "TestUser2", "test2@mail.com");
    private final ItemRequest newItemRequest1 = new ItemRequest(null, "TestRequest1", user1,
            LocalDateTime.of(2022, 1, 1, 0, 0));
    private final ItemRequest itemRequest1 = new ItemRequest(1L, "TestRequest1", user1,
            LocalDateTime.of(2022, 1, 1, 0, 0));
    private final Item newItem1 = new Item(null, "TestItem1", "TestDescription1", true, user1, itemRequest1);
    private final Item newItem2 = new Item(null, "TestItem2", "TestDescription2", true, user2, null);
    private final Item newItem3 = new Item(null, "TestItem3", "TestDescription3", true, user2, null);
    private final Item item1 = new Item(1L, "TestItem1", "TestDescription1", true, user1, itemRequest1);
    private final Item item2 = new Item(2L, "TestItem2", "TestDescription2", true, user2, null);
    private final Item item3 = new Item(3L, "TestItem3", "TestDescription3", true, user2, null);

    @Test
    void getItemByIdTestOk() {
        manager.persist(newUser1);
        manager.persist(newItemRequest1);
        manager.persist(newItem1);
        Item itemTest = itemService.getItemById(1L);
        Assertions.assertNotNull(itemTest);
        Assertions.assertEquals(item1, itemTest);
    }

    @Test
    void getItemByIdTestWithIncorrectId() {
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemById(1L));
    }

    @Test
    void getAllItemsByOwnerIdTestOk() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItemRequest1);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newItem3);
        List<Item> items = itemService.getAllItemsByOwnerId(2L, 0, 10);
        Assertions.assertNotNull(items);
        Assertions.assertEquals(2, items.size());
        Assertions.assertEquals(item2, items.get(0));
        Assertions.assertEquals(item3, items.get(1));
    }

    @Test
    void createItemTestOk() {
        manager.persist(newUser1);
        manager.persist(newItemRequest1);
        itemService.createItem(1L, new Item(null, "TestItem1", "TestDescription1", true, null, itemRequest1));
        String query = "SELECT i FROM Item i WHERE i.id = 1";
        TypedQuery<Item> typedQuery = manager.createQuery(query, Item.class);
        Item itemTest = typedQuery.getSingleResult();
        Assertions.assertNotNull(itemTest);
        Assertions.assertEquals(1L, itemTest.getId());
        Assertions.assertEquals("TestItem1", itemTest.getName());
        Assertions.assertEquals("TestDescription1", itemTest.getDescription());
        Assertions.assertEquals(true, itemTest.getAvailable());
        Assertions.assertEquals(user1, itemTest.getOwner());
        Assertions.assertEquals(itemRequest1, itemTest.getRequest());
    }

    @Test
    void updateItemTestOk() {
        manager.persist(newUser1);
        manager.persist(newItemRequest1);
        manager.persist(newItem1);
        itemService.updateItem(1L, new Item(1L, "Update", "Update", false, null, null));
        String query = "SELECT i FROM Item i WHERE i.id = 1";
        TypedQuery<Item> typedQuery = manager.createQuery(query, Item.class);
        Item itemTest = typedQuery.getSingleResult();
        Assertions.assertNotNull(itemTest);
        Assertions.assertEquals(1L, itemTest.getId());
        Assertions.assertEquals("Update", itemTest.getName());
        Assertions.assertEquals("Update", itemTest.getDescription());
        Assertions.assertEquals(false, itemTest.getAvailable());
        Assertions.assertEquals(user1, itemTest.getOwner());
        Assertions.assertEquals(itemRequest1, itemTest.getRequest());
    }

    @Test
    void searchItemsByTextInNameAndDescriptionTestOk() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItemRequest1);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newItem3);
        List<Item> items = itemService.searchItemsByTextInNameAndDescription("TestItem1", 0, 10);
        Assertions.assertNotNull(items);
        Assertions.assertEquals(1, items.size());
        Assertions.assertEquals(item1, items.get(0));
    }

    @Test
    void deleteItemByIdTestOk() {
        manager.persist(newUser1);
        manager.persist(newItemRequest1);
        manager.persist(newItem1);
        itemService.deleteItemById(1L, 1L);
        String query = "SELECT i FROM Item i WHERE i.id = 1";
        TypedQuery<Item> typedQuery = manager.createQuery(query, Item.class);
        Assertions.assertThrows(NoResultException.class, typedQuery::getSingleResult);
    }

    @Test
    void createCommentTest() {
        Booking booking = new Booking(null, LocalDateTime.now().minusDays(2L), LocalDateTime.now().minusDays(1L),
                item1, user1, BookingStatus.APPROVED);
        manager.persist(newUser1);
        manager.persist(newItemRequest1);
        manager.persist(newItem1);
        manager.persist(booking);
        itemService.createComment(1L, 1L, new Comment(null, "CommentTest", item1, user1, LocalDateTime.now()));
        String query = "SELECT c FROM Comment c WHERE c.id = 1";
        TypedQuery<Comment> typedQuery = manager.createQuery(query, Comment.class);
        Comment comment = typedQuery.getSingleResult();
        Assertions.assertNotNull(comment);
        Assertions.assertEquals(1L, comment.getId());
        Assertions.assertEquals("CommentTest", comment.getText());
        Assertions.assertEquals(item1, comment.getItem());
        Assertions.assertEquals(user1, comment.getAuthor());
    }
}
