package ru.practicum.shareit.booking;

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
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.booking.service.BookingServiceImpl;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class BookingServiceTest {

    private BookingService bookingService;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserService userService;
    @Mock
    private ItemService itemService;
    private final Booking newBooking = new Booking(null, LocalDateTime.of(2025, 1, 10, 0, 0),
            LocalDateTime.of(2025, 1, 12, 0, 0), null, null, null);
    private final User user1 = new User(1L, "TestUser1", "test1@mail.com");
    private final User user2 = new User(2L, "TestUser2", "test2@mail.com");
    private final Item item1 = new Item(1L, "TestItem1", "TestDescription1", true, user2, null);
    private final Item item2 = new Item(2L, "TestItem", "TestDescription1", false, user2, null);
    private final Booking booking = new Booking(1L, LocalDateTime.of(2025, 1, 10, 0, 0),
            LocalDateTime.of(2025, 1, 12, 0, 0), item1, user1, BookingStatus.WAITING);

    @BeforeEach
    void beforeEach() {
        bookingService = new BookingServiceImpl(bookingRepository, userService, itemService);
    }

    @Test
    void createBookingTestOk() {
        Mockito.when(itemService.getItemById(1L)).thenReturn(item1);
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(booking);
        Booking booking1 = bookingService.createBooking(1L, 1L, newBooking);
        Assertions.assertNotNull(booking1);
        Assertions.assertEquals(booking, booking1);
    }

    @Test
    void createBookingForOwnItem() {
        Mockito.when(itemService.getItemById(1L)).thenReturn(item1);
        Mockito.when(userService.getUserById(1L)).thenReturn(user2);
        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.createBooking(1L, 1L, newBooking));
    }

    @Test
    void createBookingWithNotAvailableItem() {
        Mockito.when(itemService.getItemById(2L)).thenReturn(item2);
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.createBooking(1L, 2L, newBooking));
    }

    @Test
    void createBookingWithEndBeforeStart() {
        Booking wrongBooking = new Booking(null, LocalDateTime.of(2025, 1, 10, 0, 0),
                LocalDateTime.of(2024, 1, 12, 0, 0), null, null, null);
        Mockito.when(itemService.getItemById(1L)).thenReturn(item1);
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.createBooking(1L, 1L, wrongBooking));
    }

    @Test
    void getBookingByIdByBookerTestOk() {
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Booking booking1 = bookingService.getBookingById(1L, 1L);
        Assertions.assertNotNull(booking1);
        Assertions.assertEquals(booking, booking1);
    }

    @Test
    void getBookingByIdByOwnerTestOk() {
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Booking booking1 = bookingService.getBookingById(2L, 1L);
        Assertions.assertNotNull(booking1);
        Assertions.assertEquals(booking, booking1);
    }

    @Test
    void getBookingByIdWithIncorrectId() {
        Mockito.when(bookingRepository.findById(Mockito.anyLong())).thenReturn(Optional.empty());
        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.getBookingById(1L, 1L));
    }

    @Test
    void updateBookingStatusAPPROVED() {
        Booking bookingUpdated = new Booking(1L, LocalDateTime.of(2025, 1, 10, 0, 0),
                LocalDateTime.of(2025, 1, 12, 0, 0), item1, user1, BookingStatus.APPROVED);
        Mockito.when(itemService.getItemById(1L)).thenReturn(item1);
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(bookingUpdated);
        Booking booking1 = bookingService.updateBookingStatus(2L, 1L, true);
        Assertions.assertNotNull(booking1);
        Assertions.assertEquals(bookingUpdated, booking1);
    }

    @Test
    void updateBookingStatusREJECTED() {
        Booking bookingUpdated = new Booking(1L, LocalDateTime.of(2025, 1, 10, 0, 0),
                LocalDateTime.of(2025, 1, 12, 0, 0), item1, user1, BookingStatus.REJECTED);
        Mockito.when(itemService.getItemById(1L)).thenReturn(item1);
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Mockito.when(bookingRepository.save(Mockito.any(Booking.class))).thenReturn(bookingUpdated);
        Booking booking1 = bookingService.updateBookingStatus(2L, 1L, false);
        Assertions.assertNotNull(booking1);
        Assertions.assertEquals(bookingUpdated, booking1);
    }

    @Test
    void updateBookingStatusAlreadyAPPROVED() {
        Booking bookingUpdated = new Booking(1L, LocalDateTime.of(2025, 1, 10, 0, 0),
                LocalDateTime.of(2025, 1, 12, 0, 0), item1, user1, BookingStatus.APPROVED);
        Mockito.when(itemService.getItemById(1L)).thenReturn(item1);
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingUpdated));
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.updateBookingStatus(2L, 1L, true));
    }

    @Test
    void updateBookingStatusAlreadyREJECTED() {
        Booking bookingUpdated = new Booking(1L, LocalDateTime.of(2025, 1, 10, 0, 0),
                LocalDateTime.of(2025, 1, 12, 0, 0), item1, user1, BookingStatus.REJECTED);
        Mockito.when(itemService.getItemById(1L)).thenReturn(item1);
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(bookingUpdated));
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.updateBookingStatus(2L, 1L, false));
    }

    @Test
    void updateBookingStatusByNotOwner() {
        Mockito.when(itemService.getItemById(1L)).thenReturn(item1);
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Mockito.when(bookingRepository.findById(1L)).thenReturn(Optional.of(booking));
        Assertions.assertThrows(NotFoundException.class,
                () -> bookingService.updateBookingStatus(1L, 1L, false));
    }

    @Test
    void getBookingsByUserIdAndStateALL() {
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Mockito.when(bookingRepository.findAllByBookerId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getBookingsByUserIdAndState(1L, "ALL", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getBookingsByUserIdAndStatePAST() {
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Mockito.when(bookingRepository.findPastByBookerId(Mockito.anyLong(), Mockito.any(LocalDateTime.class),
                Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getBookingsByUserIdAndState(1L, "PAST", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getBookingsByUserIdAndStateFUTURE() {
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Mockito.when(bookingRepository.findFutureByBookerId(Mockito.anyLong(), Mockito.any(LocalDateTime.class),
                Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getBookingsByUserIdAndState(1L, "FUTURE", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getBookingsByUserIdAndStateCURRENT() {
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Mockito.when(bookingRepository.findCurrentByBookerId(Mockito.anyLong(), Mockito.any(LocalDateTime.class),
                Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getBookingsByUserIdAndState(1L, "CURRENT", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getBookingsByUserIdAndStateWAITING() {
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Mockito.when(bookingRepository.findByBookerIdAndStatus(Mockito.anyLong(), Mockito.any(BookingStatus.class),
                Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getBookingsByUserIdAndState(1L, "WAITING", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getBookingsByUserIdAndStateREJECTED() {
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Mockito.when(bookingRepository.findByBookerIdAndStatus(Mockito.anyLong(), Mockito.any(BookingStatus.class),
                Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getBookingsByUserIdAndState(1L, "REJECTED", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getBookingsByUserIdWithIncorrectState() {
        Mockito.when(userService.getUserById(1L)).thenReturn(user1);
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.getBookingsByUserIdAndState(1L, "WTF", 0, 10));
    }

    @Test
    void getOwnerBookingsALL() {
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Mockito.when(bookingRepository.findAllByOwnerId(Mockito.anyLong(), Mockito.any(Pageable.class)))
                .thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getOwnerBookings(2L, "ALL", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getOwnerBookingsWithStatePAST() {
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Mockito.when(bookingRepository.findPastByOwnerId(Mockito.anyLong(), Mockito.any(LocalDateTime.class),
                Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getOwnerBookings(2L, "PAST", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getOwnerBookingsWithStateFUTURE() {
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Mockito.when(bookingRepository.findFutureByOwnerId(Mockito.anyLong(), Mockito.any(LocalDateTime.class),
                Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getOwnerBookings(2L, "FUTURE", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getOwnerBookingsWithStateCURRENT() {
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Mockito.when(bookingRepository.findCurrentByOwnerId(Mockito.anyLong(), Mockito.any(LocalDateTime.class),
                Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getOwnerBookings(2L, "CURRENT", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getOwnerBookingsWithStateWAITING() {
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Mockito.when(bookingRepository.findByOwnerIdAndStatus(Mockito.anyLong(), Mockito.any(BookingStatus.class),
                Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getOwnerBookings(2L, "WAITING", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getOwnerBookingsWithStateREJECTED() {
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Mockito.when(bookingRepository.findByOwnerIdAndStatus(Mockito.anyLong(), Mockito.any(BookingStatus.class),
                Mockito.any(Pageable.class))).thenReturn(List.of(booking));
        List<Booking> bookings = bookingService.getOwnerBookings(2L, "REJECTED", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(booking, bookings.get(0));
    }

    @Test
    void getOwnerBookingsWithStateIncorrect() {
        Mockito.when(userService.getUserById(2L)).thenReturn(user2);
        Assertions.assertThrows(ValidationException.class,
                () -> bookingService.getOwnerBookings(2L, "WTF", 0, 10));
    }
}
