package ru.practicum.shareit.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.item.dto.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.request.controller.ItemRequestController;
import ru.practicum.shareit.request.dto.ItemRequestDto;
import ru.practicum.shareit.request.dto.ItemRequestMapper;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ItemRequestController.class)
public class ItemRequestControllerTest {
    @MockBean
    private ItemRequestService itemRequestService;
    @MockBean
    private ItemService itemService;
    @SpyBean
    private ItemRequestMapper itemRequestMapper;
    @SpyBean
    private ItemMapper itemMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private final ItemDto itemDto = new ItemDto(1L, "Item_1", "Item_Test", true,
            new ItemDto.UserDto(1L, "User-Test"), 1L, null, null, null);
    private final ItemRequest newItemRequest = new ItemRequest(null, "TestRequest", null, null);
    private final ItemRequestDto itemRequestDto = new ItemRequestDto(1L, "TestRequest",
            1L, null, List.of(itemDto));
    private final ItemRequest itemRequest = new ItemRequest(1L, "TestRequest",
            new User(1L, "test", "test@mail.com"), null);
    private final Item item = new Item(1L, "Item_1", "Item_Test",
            true, new User(), itemRequest);

    @Test
    void getAllRequestsByPageTestOk() throws Exception {
        Mockito.when(itemRequestService.getAllRequestsByPage(1L, 1, 1))
                .thenReturn(List.of(itemRequest));
        Mockito.when(itemService.getItemsByRequestId(1L)).thenReturn(List.of(item));
        mockMvc.perform(get("/requests/all?from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].requesterId").value(itemRequestDto.getRequesterId()))
                .andExpect(jsonPath("$[0].created").value(itemRequestDto.getCreated()))
                .andExpect(jsonPath("$[0].items[0].id")
                        .value(itemRequestDto.getItems().get(0).getId()))
                .andExpect(jsonPath("$[0].items[0].name")
                        .value(itemRequestDto.getItems().get(0).getName()))
                .andExpect(jsonPath("$[0].items[0].description")
                        .value(itemRequestDto.getItems().get(0).getDescription()));
    }

    @Test
    void getAllRequestsByPageWithDefaultPagination() throws Exception {
        Mockito.when(itemRequestService.getAllRequestsByPage(1L, 0, 10))
                .thenReturn(List.of(itemRequest));
        Mockito.when(itemService.getItemsByRequestId(1L)).thenReturn(List.of(item));
        mockMvc.perform(get("/requests/all")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].requesterId").value(itemRequestDto.getRequesterId()))
                .andExpect(jsonPath("$[0].created").value(itemRequestDto.getCreated()))
                .andExpect(jsonPath("$[0].items[0].id")
                        .value(itemRequestDto.getItems().get(0).getId()))
                .andExpect(jsonPath("$[0].items[0].name")
                        .value(itemRequestDto.getItems().get(0).getName()))
                .andExpect(jsonPath("$[0].items[0].description")
                        .value(itemRequestDto.getItems().get(0).getDescription()));
    }

    @Test
    void createItemRequestTestOk() throws Exception {
        Mockito.when(itemRequestService.createItemRequest(Mockito.anyLong(),
                        Mockito.any(ItemRequest.class)))
                .thenReturn(itemRequest);
        mockMvc.perform(post("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(newItemRequest))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.requesterId").value(itemRequestDto.getRequesterId()))
                .andExpect(jsonPath("$.created").value(itemRequestDto.getCreated()));
    }

    @Test
    void getAllRequestsByOwnerIdTestOk() throws Exception {
        Mockito.when(itemRequestService.getAllRequestsByOwnerId(1L)).thenReturn(List.of(itemRequest));
        Mockito.when(itemService.getItemsByRequestId(Mockito.anyLong())).thenReturn(List.of(item));
        mockMvc.perform(get("/requests")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$[0].requesterId").value(itemRequestDto.getRequesterId()))
                .andExpect(jsonPath("$[0].created").value(itemRequestDto.getCreated()))
                .andExpect(jsonPath("$[0].items[0].id")
                        .value(itemRequestDto.getItems().get(0).getId()))
                .andExpect(jsonPath("$[0].items[0].name")
                        .value(itemRequestDto.getItems().get(0).getName()))
                .andExpect(jsonPath("$[0].items[0].description")
                        .value(itemRequestDto.getItems().get(0).getDescription()));
    }

    @Test
    void getItemRequestByIdTestOk() throws Exception {
        Mockito.when(itemRequestService.getItemRequestById(1L, 1L)).thenReturn(itemRequest);
        Mockito.when(itemService.getItemsByRequestId(Mockito.anyLong())).thenReturn(List.of(item));
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.description").value(itemRequestDto.getDescription()))
                .andExpect(jsonPath("$.requesterId").value(itemRequestDto.getRequesterId()))
                .andExpect(jsonPath("$.created").value(itemRequestDto.getCreated()))
                .andExpect(jsonPath("$.items[0].id")
                        .value(itemRequestDto.getItems().get(0).getId()))
                .andExpect(jsonPath("$.items[0].name")
                        .value(itemRequestDto.getItems().get(0).getName()))
                .andExpect(jsonPath("$.items[0].description")
                        .value(itemRequestDto.getItems().get(0).getDescription()));
    }

    @Test
    void getItemRequestByIdWithoutRequest() throws Exception {
        Mockito.when(itemRequestService.getItemRequestById(1L, 1L))
                .thenThrow(NotFoundException.class);
        mockMvc.perform(get("/requests/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

}
