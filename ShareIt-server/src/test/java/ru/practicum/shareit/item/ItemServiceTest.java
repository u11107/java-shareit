package ru.practicum.shareit.item;

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
import org.springframework.data.domain.PageRequest;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.repository.CommentRepository;
import ru.practicum.shareit.item.repository.ItemRepository;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.item.service.ItemServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemServiceTest {
    private ItemService itemService;
    @Mock
    private ItemRepository itemRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private CommentRepository commentRepository;
    private final User user = new User(1L, "User", "user@mail.com");
    private final Item item = new Item(1L, "TestItem", "TestDescription", true,
            user, null);
    private final Item newItem = new Item(null, "TestItem", "TestDescription", true,
            null, null);
    private final Comment comment = new Comment(1L, "Comment", item, user, LocalDateTime.MIN);
    private final Comment newComment = new Comment(null, "Comment", null, null, null);

    @BeforeEach
    void beforeEach() {
        itemService = new ItemServiceImpl(itemRepository, bookingRepository, userService, commentRepository);
    }

    @Test
    void getItemByIdTestOk() {
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Item item1 = itemService.getItemById(1L);
        Assertions.assertNotNull(item1);
        Assertions.assertEquals(item, item1);
    }

    @Test
    void getItemByIdTestWithIncorrectId() {
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> itemService.getItemById(1L));
    }

    @Test
    void getAllItemsByOwnerIdTestOk() {
        Mockito.when(itemRepository.findByOwnerId(1L, PageRequest.of(0, 10)))
                .thenReturn(List.of(item));
        List<Item> itemList = itemService.getAllItemsByOwnerId(1L, 0, 10);
        Assertions.assertNotNull(itemList);
        Assertions.assertEquals(1, itemList.size());
        Assertions.assertEquals(item, itemList.get(0));
    }

    @Test
    void getAllItemsByOwnerIdTestWithoutItems() {
        Mockito.when(itemRepository.findByOwnerId(1L, PageRequest.of(0, 10)))
                .thenReturn(Collections.emptyList());
        List<Item> itemList = itemService.getAllItemsByOwnerId(1L, 0, 10);
        Assertions.assertNotNull(itemList);
        Assertions.assertEquals(0, itemList.size());
    }

    @Test
    void createItemTestOk() {
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(user);
        Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(item);
        Item item1 = itemService.createItem(1L, newItem);
        Assertions.assertNotNull(item1);
        Assertions.assertEquals(item, item1);
    }

    @Test
    void updateItemTestOk() {
        Item toUpdateItem = new Item(1L, "Update", "Update", false, null, null);
        Item updatedItem = new Item(1L, "Update", "Update", false, user, null);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(updatedItem)).thenReturn(updatedItem);
        Item item1 = itemService.updateItem(1L, toUpdateItem);
        Assertions.assertNotNull(item1);
        Assertions.assertEquals(updatedItem, item1);
    }

    @Test
    void updateItem_NameTestOk() {
        Item toUpdateItem = new Item(1L, "Update", null, null, null, null);
        Item updatedItem = new Item(1L, "Update", "TestDescription", true,
                user, null);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(updatedItem)).thenReturn(updatedItem);
        Item item1 = itemService.updateItem(1L, toUpdateItem);
        Assertions.assertNotNull(item1);
        Assertions.assertEquals(updatedItem, item1);
    }

    @Test
    void updateItem_DescriptionTestOk() {
        Item toUpdateItem = new Item(1L, null, "Update", null, null, null);
        Item updatedItem = new Item(1L, "TestItem", "Update", true,
                user, null);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(updatedItem)).thenReturn(updatedItem);
        Item item1 = itemService.updateItem(1L, toUpdateItem);
        Assertions.assertNotNull(item1);
        Assertions.assertEquals(updatedItem, item1);
    }

    @Test
    void updateItem_AvailableTestOk() {
        Item toUpdateItem = new Item(1L, null, null, false, null, null);
        Item updatedItem = new Item(1L, "TestItem", "TestDescription", false,
                user, null);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(updatedItem)).thenReturn(updatedItem);
        Item item1 = itemService.updateItem(1L, toUpdateItem);
        Assertions.assertNotNull(item1);
        Assertions.assertEquals(updatedItem, item1);
    }

    @Test
    void updateItemWithBlankNameAndDescriptionTest() {
        Item toUpdateItem = new Item(1L, " ", " ", true, null, null);
        Item updatedItem = new Item(1L, "TestItem", "TestDescription", true,
                user, null);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(itemRepository.save(updatedItem)).thenReturn(updatedItem);
        Item item1 = itemService.updateItem(1L, toUpdateItem);
        Assertions.assertNotNull(item1);
        Assertions.assertEquals(updatedItem, item1);
    }

    @Test
    void updateItemWithIncorrectIdTest() {
        Item toUpdateItem = new Item(1L, " ", " ", true, null, null);
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItem(1L, toUpdateItem));
    }

    @Test
    void updateItemWithIncorrectOwnerIdTest() {
        Item toUpdateItem = new Item(1L, " ", " ", true, null, null);
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Assertions.assertThrows(NotFoundException.class, () -> itemService.updateItem(2L, toUpdateItem));
    }

    @Test
    void searchItemsByTextInNameAndDescriptionTestOk() {
        Item searchItem = new Item(1L, "TestItem", "TestDescription", true, user, null);
        Mockito.when(itemService.searchItemsByTextInNameAndDescription("Test", 0, 10))
                .thenReturn(List.of(item));
        List<Item> itemList = itemService.searchItemsByTextInNameAndDescription("Test", 0, 10);
        Assertions.assertNotNull(itemList);
        Assertions.assertEquals(1, itemList.size());
        Assertions.assertEquals(searchItem, itemList.get(0));
    }

    @Test
    void searchItemsWithBlankTextTest() {
        List<Item> itemList = itemService.searchItemsByTextInNameAndDescription(" ", 0, 10);
        Assertions.assertNotNull(itemList);
        Assertions.assertEquals(0, itemList.size());
    }

    @Test
    void deleteItemWithIncorrectItemId() {
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class, () -> itemService.deleteItemById(1L, 1L));
    }

    @Test
    void deleteItemWithIncorrectOwnerId() {
        Mockito.when(itemRepository.findById(Mockito.anyLong())).thenReturn(Optional.of(item));
        Assertions.assertThrows(ValidationException.class, () -> itemService.deleteItemById(2L, 1L));
    }

    @Test
    void createCommentTest() {
        Booking booking = new Booking();
        Mockito.when(bookingRepository.findCompletedBooking(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.any(LocalDateTime.class))).thenReturn(booking);
        Mockito.when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
        Mockito.when(userService.getUserById(1L)).thenReturn(user);
        Mockito.when(commentRepository.save(Mockito.any(Comment.class)))
                .thenReturn(comment);
        Comment comment1 = itemService.createComment(1L, 1L, newComment);
        Assertions.assertNotNull(comment1);
        Assertions.assertEquals(comment, comment1);
    }

    @Test
    void createCommentWithoutBookingTest() {
        Mockito.when(bookingRepository.findCompletedBooking(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.any(LocalDateTime.class))).thenReturn(null);
        Assertions.assertThrows(ValidationException.class, () -> itemService.createComment(1L, 1L, newComment));
    }
}
