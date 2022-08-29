package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.controller.ItemController;
import ru.practicum.shareit.item.dto.CommentMapper;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Comment;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemController.class)
public class ItemControllerTest {
    @MockBean
    private ItemService itemService;
    @MockBean
    private BookingService bookingService;
    @MockBean
    private ItemRequestService itemRequestService;
    @SpyBean
    private ItemMapper itemMapper;
    @SpyBean
    private CommentMapper commentMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private final User user = new User(1L, "User", "user@mail.com");
    private final Item item = new Item(1L, "TestItem", "TestDescription", true,
            user, null);
    private final Item newItem = new Item(null, "TestItem", "TestDescription", true,
            null, null);
    private final Comment comment = new Comment(1L, "Comment", item, user, LocalDateTime.MIN);
    private final Comment newComment = new Comment(null, "Comment", null, null, null);

    @Test
    void getItemByIdTestOk() throws Exception {
        Mockito.when(itemService.getItemById(Mockito.anyLong())).thenReturn(item);
        Mockito.when(itemService.getCommentsByItemId(Mockito.anyLong())).thenReturn(List.of(comment));
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.owner.id").value(item.getOwner().getId()))
                .andExpect(jsonPath("$.owner.name").value(item.getOwner().getName()))
                .andExpect(jsonPath("$.comments[0].id").value(comment.getId()))
                .andExpect(jsonPath("$.comments[0].text").value(comment.getText()));
    }

    @Test
    void getItemByIdWithIncorrectId() throws Exception {
        Mockito.when(itemService.getItemById(Mockito.anyLong())).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllItemsByOwnerIdTestOk() throws Exception {
        Mockito.when(itemService.getAllItemsByOwnerId(1L, 1, 1)).thenReturn(List.of(item));
        Mockito.when(itemService.getCommentsByItemId(Mockito.anyLong())).thenReturn(List.of(comment));
        mockMvc.perform(get("/items?from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item.getAvailable()))
                .andExpect(jsonPath("$[0].owner.id").value(item.getOwner().getId()))
                .andExpect(jsonPath("$[0].owner.name").value(item.getOwner().getName()))
                .andExpect(jsonPath("$[0].comments[0].id").value(comment.getId()))
                .andExpect(jsonPath("$[0].comments[0].text").value(comment.getText()));
    }

    @Test
    void getAllItemsByOwnerIdWithDefaultPagination() throws Exception {
        Mockito.when(itemService.getAllItemsByOwnerId(1L, 0, 10)).thenReturn(List.of(item));
        Mockito.when(itemService.getCommentsByItemId(Mockito.anyLong())).thenReturn(List.of(comment));
        mockMvc.perform(get("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item.getAvailable()))
                .andExpect(jsonPath("$[0].owner.id").value(item.getOwner().getId()))
                .andExpect(jsonPath("$[0].owner.name").value(item.getOwner().getName()))
                .andExpect(jsonPath("$[0].comments[0].id").value(comment.getId()))
                .andExpect(jsonPath("$[0].comments[0].text").value(comment.getText()));
    }

    @Test
    void createItemTestOk() throws Exception {
        Mockito.when(itemService.createItem(1L, newItem)).thenReturn(item);
        mockMvc.perform(post("/items")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(newItem))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(item.getId()))
                .andExpect(jsonPath("$.name").value(item.getName()))
                .andExpect(jsonPath("$.description").value(item.getDescription()))
                .andExpect(jsonPath("$.available").value(item.getAvailable()))
                .andExpect(jsonPath("$.owner.id").value(item.getOwner().getId()))
                .andExpect(jsonPath("$.owner.name").value(item.getOwner().getName()));
    }

    @Test
    void updateItemTestOk() throws Exception {
        Mockito.when(itemService.updateItem(Mockito.anyLong(), Mockito.any(Item.class)))
                .thenReturn(item);
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(newItem))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void updateItemTestWithIncorrectItemId() throws Exception {
        Mockito.when(itemService.updateItem(Mockito.anyLong(), Mockito.any(Item.class)))
                .thenThrow(NotFoundException.class);
        mockMvc.perform(patch("/items/1")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(newItem))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void searchItemsByTextInNameAndDescriptionTestOk() throws Exception {
        Mockito.when(itemService.searchItemsByTextInNameAndDescription("text", 1, 1))
                .thenReturn(List.of(item));
        mockMvc.perform(get("/items/search?text=text&from=1&size=1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item.getAvailable()))
                .andExpect(jsonPath("$[0].owner.id").value(item.getOwner().getId()))
                .andExpect(jsonPath("$[0].owner.name").value(item.getOwner().getName()));
    }

    @Test
    void searchItemsByTextInNameAndDescriptionTestWithDefaultPagination() throws Exception {
        Mockito.when(itemService.searchItemsByTextInNameAndDescription("text", 0, 10))
                .thenReturn(List.of(item));
        mockMvc.perform(get("/items/search?text=text")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(item.getId()))
                .andExpect(jsonPath("$[0].description").value(item.getDescription()))
                .andExpect(jsonPath("$[0].available").value(item.getAvailable()))
                .andExpect(jsonPath("$[0].owner.id").value(item.getOwner().getId()))
                .andExpect(jsonPath("$[0].owner.name").value(item.getOwner().getName()));
    }

    @Test
    void createCommentTestOk() throws Exception {
        Mockito.when(itemService.createComment(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.any(Comment.class))).thenReturn(comment);
        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(newComment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(comment.getId()))
                .andExpect(jsonPath("$.text").value(comment.getText()))
                .andExpect(jsonPath("$.authorName").value(comment.getAuthor().getName()));
    }

    @Test
    void createCommentTestValidationException() throws Exception {
        Mockito.when(itemService.createComment(Mockito.anyLong(), Mockito.anyLong(),
                Mockito.any(Comment.class))).thenThrow(ValidationException.class);
        mockMvc.perform(post("/items/1/comment")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(newComment))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

    }
}
