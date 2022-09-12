package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@SpringBootTest
@Transactional
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingServiceIntegrationTest {

    private final EntityManager manager;
    private final BookingService bookingService;

    private final User user1 = new User(1L, "TestUser1", "test1@mail.com");
    private final User user2 = new User(2L, "TestUser2", "test2@mail.com");
    private final User newUser1 = new User(null, "TestUser1", "test1@mail.com");
    private final User newUser2 = new User(null, "TestUser2", "test2@mail.com");
    private final Item newItem1 = new Item(null, "TestItem1", "TestDescription1", true, user1, null);
    private final Item newItem2 = new Item(null, "TestItem2", "TestDescription2", true, user2, null);
    private final Item item1 = new Item(1L, "TestItem1", "TestDescription1", true, user1, null);
    private final Item item2 = new Item(2L, "TestItem2", "TestDescription2", true, user2, null);
    private final Booking newBooking1 = new Booking(null, LocalDateTime.of(2025, 1, 10, 0, 0),
            LocalDateTime.of(2025, 1, 12, 0, 0), item1, user2, BookingStatus.WAITING);
    private final Booking newBooking2 = new Booking(null, LocalDateTime.of(2025, 1, 10, 0, 0),
            LocalDateTime.of(2025, 1, 12, 0, 0), item2, user1, BookingStatus.WAITING);
    private final Booking booking = new Booking(1L, LocalDateTime.of(2025, 1, 10, 0, 0),
            LocalDateTime.of(2025, 1, 12, 0, 0), item1, user2, BookingStatus.WAITING);

    @Test
    void createBookingTest() {
        Booking toCreateBooking = new Booking(null, LocalDateTime.now().plusDays(1),
                LocalDateTime.now().plusDays(2), null, null, null);
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        bookingService.createBooking(2L, 1L, toCreateBooking);
        String query = "SELECT b FROM Booking b WHERE b.id = 1";
        TypedQuery<Booking> typedQuery = manager.createQuery(query, Booking.class);
        Booking booking1 = typedQuery.getSingleResult();
        Assertions.assertNotNull(booking1);
        Assertions.assertEquals(1L, booking1.getId());
    }

    @Test
    void getBookingByIdTest() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newBooking1);
        Booking booking1 = bookingService.getBookingById(1L, 1L);
        Assertions.assertNotNull(booking1);
        Assertions.assertEquals(booking, booking1);
    }

    @Test
    void updateBookingStatusTest() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newBooking1);
        bookingService.updateBookingStatus(1L, 1L, true);
        String query = "SELECT b FROM Booking b WHERE b.id = 1";
        TypedQuery<Booking> typedQuery = manager.createQuery(query, Booking.class);
        Booking booking1 = typedQuery.getSingleResult();
        Assertions.assertNotNull(booking1);
        Assertions.assertEquals(BookingStatus.APPROVED, booking1.getStatus());
    }

    @Test
    void getBookingsByUserIdAndStateTest() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        List<Booking> bookings = bookingService.getBookingsByUserIdAndState(1L, "ALL", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(2, bookings.get(0).getId());
    }

    @Test
    void getOwnerBookingsTest() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        List<Booking> bookings = bookingService.getOwnerBookings(1L, "ALL", 0, 10);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1, bookings.get(0).getId());
    }
}
