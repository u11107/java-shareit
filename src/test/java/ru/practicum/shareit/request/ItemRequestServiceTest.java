package ru.practicum.shareit.request;

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
import org.springframework.data.domain.Pageable;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.request.model.ItemRequest;
import ru.practicum.shareit.request.repository.ItemRequestRepository;
import ru.practicum.shareit.request.service.ItemRequestService;
import ru.practicum.shareit.request.service.ItemRequestServiceImpl;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class ItemRequestServiceTest {
    private ItemRequestService itemRequestService;
    @Mock
    private ItemRequestRepository itemRequestRepository;
    @Mock
    private UserService userService;
    private final User user = new User(1L, "TestUser", "test@mail.com");
    private final ItemRequest newItemRequest = new ItemRequest(null, "TestDescription", null, null);
    private final ItemRequest itemRequest = new ItemRequest(1L, "TestDescription", user, LocalDateTime.MIN);

    @BeforeEach
    void beforeEach() {
        itemRequestService = new ItemRequestServiceImpl(itemRequestRepository, userService);
    }

    @Test
    void getItemRequestByIdTestOk() {
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(itemRequestRepository.findById(1L)).thenReturn(Optional.of(itemRequest));
        ItemRequest itemRequest1 = itemRequestService.getItemRequestById(1L, 1L);
        Assertions.assertNotNull(itemRequest1);
        Assertions.assertEquals(itemRequest, itemRequest1);
    }

    @Test
    void getItemRequestByIdWithIncorrectId() {
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(itemRequestRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> itemRequestService.getItemRequestById(1L, 1L));
    }

    @Test
    void createItemRequestTestOk() {
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(user);
        Mockito.when(itemRequestRepository.save(newItemRequest))
                .thenReturn(itemRequest);
        ItemRequest itemRequest1 = itemRequestService.createItemRequest(1L, newItemRequest);
        Assertions.assertNotNull(itemRequest1);
        Assertions.assertEquals(itemRequest, itemRequest1);
    }

    @Test
    void getAllRequestsByPageTestOk() {
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(itemRequestRepository.findAllByIdNot(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(List.of(itemRequest));
        List<ItemRequest> itemRequestList = itemRequestService.getAllRequestsByPage(1L, 0, 10);
        Assertions.assertNotNull(itemRequestList);
        Assertions.assertEquals(itemRequest, itemRequestList.get(0));
    }

    @Test
    void getAllRequestsByPageWithoutRequests() {
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(itemRequestRepository.findAllByIdNot(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(Collections.emptyList());
        List<ItemRequest> itemRequestList = itemRequestService.getAllRequestsByPage(1L, 0, 10);
        Assertions.assertNotNull(itemRequestList);
        Assertions.assertEquals(0, itemRequestList.size());
    }

    @Test
    void getAllRequestsByOwnerIdTestOk() {
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(1L))
                .thenReturn(List.of(itemRequest));
        List<ItemRequest> itemRequestList = itemRequestService.getAllRequestsByOwnerId(1L);
        Assertions.assertNotNull(itemRequestList);
        Assertions.assertEquals(itemRequest, itemRequestList.get(0));
    }

    @Test
    void getAllRequestsByOwnerIdWithoutRequests() {
        Mockito.when(userService.getUserById(Mockito.anyLong())).thenReturn(null);
        Mockito.when(itemRequestRepository.findAllByRequesterIdOrderByCreatedDesc(1L))
                .thenReturn(Collections.emptyList());
        List<ItemRequest> itemRequestList = itemRequestService.getAllRequestsByOwnerId(1L);
        Assertions.assertNotNull(itemRequestList);
        Assertions.assertEquals(0, itemRequestList.size());
    }
}
