package ru.practicum.shareit.booking;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.annotation.DirtiesContext;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class BookingRepositoryTest {
    @Autowired
    private TestEntityManager manager;
    @Autowired
    private BookingRepository repository;
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
    private final Pageable pageable = PageRequest.of(0, 10);

    @Test
    void findAllByBookerIdTest() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        List<Booking> bookings = repository.findAllByBookerId(2L, pageable);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findPastByBookerId() {
        Booking newBookingPast = new Booking(null, LocalDateTime.of(2021, 1, 10, 0, 0),
                LocalDateTime.of(2021, 1, 12, 0, 0), item1, user2, BookingStatus.WAITING);
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBookingPast);
        List<Booking> bookings = repository.findPastByBookerId(2L, LocalDateTime.now(), pageable);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findFutureByBookerId() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        List<Booking> bookings = repository.findFutureByBookerId(2L, LocalDateTime.now(), pageable);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findCurrentByBookerId() {
        Booking newBookingCurrent = new Booking(null, LocalDateTime.of(2021, 1, 10, 0, 0),
                LocalDateTime.of(2025, 1, 12, 0, 0), item1, user2, BookingStatus.WAITING);
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBookingCurrent);
        List<Booking> bookings = repository.findCurrentByBookerId(2L, LocalDateTime.now(), pageable);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findByBookerIdAndStatus() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        List<Booking> bookings = repository.findByBookerIdAndStatus(2L, BookingStatus.WAITING, pageable);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findAllByOwnerIdTest() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        List<Booking> bookings = repository.findAllByOwnerId(2L, pageable);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(2L, bookings.get(0).getId());
    }

    @Test
    void findPastByOwnerIdTest() {
        Booking newBookingPast = new Booking(null, LocalDateTime.of(2021, 1, 10, 0, 0),
                LocalDateTime.of(2021, 1, 12, 0, 0), item2, user1, BookingStatus.WAITING);
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBookingPast);
        List<Booking> bookings = repository.findPastByOwnerId(2L, LocalDateTime.now(), pageable);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findFutureByOwnerIdTest() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        List<Booking> bookings = repository.findFutureByOwnerId(2L, LocalDateTime.now(), pageable);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(2L, bookings.get(0).getId());
    }

    @Test
    void findCurrentByOwnerIdTest() {
        Booking newBookingCurrent = new Booking(null, LocalDateTime.of(2021, 1, 10, 0, 0),
                LocalDateTime.of(2025, 1, 12, 0, 0), item2, user1, BookingStatus.WAITING);
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBookingCurrent);
        List<Booking> bookings = repository.findCurrentByOwnerId(2L, LocalDateTime.now(), pageable);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(1L, bookings.get(0).getId());
    }

    @Test
    void findByOwnerIdAndStatusTest() {
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        List<Booking> bookings = repository.findByOwnerIdAndStatus(2L, BookingStatus.WAITING, pageable);
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(2L, bookings.get(0).getId());
    }

    @Test
    void findCompletedBookingTest() {
        Booking newBooking = new Booking(null, LocalDateTime.now().minusDays(2L),
                LocalDateTime.now().minusDays(1L), item2, user1, BookingStatus.APPROVED);
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        manager.persist(newBooking);
        Booking bookingTest = repository.findCompletedBooking(1L, 2L, LocalDateTime.now());
        Assertions.assertNotNull(bookingTest);
        Assertions.assertEquals(3, bookingTest.getId());
    }

    @Test
    void findLastBookingsTest() {
        Booking newBooking = new Booking(null, LocalDateTime.now().minusDays(2L),
                LocalDateTime.now().minusDays(1L), item2, user1, BookingStatus.APPROVED);
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        manager.persist(newBooking);
        List<Booking> bookings = repository.findLastBookings(2L, LocalDateTime.now());
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(1, bookings.size());
        Assertions.assertEquals(3, bookings.get(0).getId());

    }

    @Test
    void findNextBookings() {
        Booking newBooking = new Booking(null, LocalDateTime.now().plusDays(2L),
                LocalDateTime.now().plusDays(3L), item2, user1, BookingStatus.APPROVED);
        manager.persist(newUser1);
        manager.persist(newUser2);
        manager.persist(newItem1);
        manager.persist(newItem2);
        manager.persist(newBooking1);
        manager.persist(newBooking2);
        manager.persist(newBooking);
        List<Booking> bookings = repository.findNextBookings(2L, LocalDateTime.now());
        Assertions.assertNotNull(bookings);
        Assertions.assertEquals(2, bookings.size());
        Assertions.assertEquals(3, bookings.get(0).getId());
        Assertions.assertEquals(2, bookings.get(1).getId());
    }
}
