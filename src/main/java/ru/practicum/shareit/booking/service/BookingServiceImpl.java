package ru.practicum.shareit.booking.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingState;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.repository.BookingRepository;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.service.ItemService;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final UserService userService;
    private final ItemService itemService;

    public BookingServiceImpl(BookingRepository bookingRepository, UserService userService, ItemService itemService) {
        this.bookingRepository = bookingRepository;
        this.userService = userService;
        this.itemService = itemService;
    }

    public Booking createBooking(Long userId, Long itemId, Booking booking) {
        Item item = itemService.getItemById(itemId);
        User booker = userService.getUserById(userId);
        validateNewBooking(booker, item, booking);
        booking.setItem(item);
        booking.setBooker(booker);
        booking.setStatus(BookingStatus.WAITING);
        return bookingRepository.save(booking);
    }

    public Booking getBookingById(Long userId, Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new NotFoundException("Введено некорректное значение id"));
        if (!userId.equals(booking.getItem().getOwner().getId())
                && !userId.equals(booking.getBooker().getId())) {
            throw new NotFoundException("Просмотр бронирования не доступен");
        }
        return booking;
    }

    public Booking updateBookingStatus(Long userId, Long bookingId, Boolean approved) {
        User user = userService.getUserById(userId);
        Booking booking = getBookingById(userId, bookingId);
        Item item = itemService.getItemById(booking.getItem().getId());
        validateUpdateBookingStatus(user, item, booking);
        if (approved) {
            booking.setStatus(BookingStatus.APPROVED);
        }
        if (!approved) {
            booking.setStatus(BookingStatus.REJECTED);
        }
        return bookingRepository.save(booking);
    }

    public List<Booking> getBookingsByUserIdAndState(Long userId, String state, Integer from, Integer size) {
        userService.getUserById(userId);
        List<Booking> bookingList = new ArrayList<>();
        BookingState bookingState;
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        Pageable pageable = PageRequest.of(from / size, size);
        switch (bookingState) {
            case ALL:
                bookingList = bookingRepository.findAllByBookerId(userId, pageable);
                break;
            case PAST:
                bookingList = bookingRepository.findPastByBookerId(userId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookingList = bookingRepository.findFutureByBookerId(userId, LocalDateTime.now(), pageable);
                break;
            case CURRENT:
                bookingList = bookingRepository.findCurrentByBookerId(userId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookingList = bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookingList = bookingRepository.findByBookerIdAndStatus(userId, BookingStatus.REJECTED, pageable);
                break;
        }
        return bookingList;
    }

    @Override
    public List<Booking> getOwnerBookings(Long userId, String state, Integer from, Integer size) {
        userService.getUserById(userId);
        List<Booking> bookingList = new ArrayList<>();
        BookingState bookingState;
        Pageable pageable = PageRequest.of(from / size, size);
        try {
            bookingState = BookingState.valueOf(state);
        } catch (IllegalArgumentException ex) {
            throw new ValidationException("Unknown state: UNSUPPORTED_STATUS");
        }
        switch (bookingState) {
            case ALL:
                bookingList = bookingRepository.findAllByOwnerId(userId, pageable);
                break;
            case PAST:
                bookingList = bookingRepository.findPastByOwnerId(userId, LocalDateTime.now(), pageable);
                break;
            case FUTURE:
                bookingList = bookingRepository.findFutureByOwnerId(userId, LocalDateTime.now(), pageable);
                break;
            case CURRENT:
                bookingList = bookingRepository.findCurrentByOwnerId(userId, LocalDateTime.now(), pageable);
                break;
            case WAITING:
                bookingList = bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.WAITING, pageable);
                break;
            case REJECTED:
                bookingList = bookingRepository.findByOwnerIdAndStatus(userId, BookingStatus.REJECTED, pageable);
                break;
        }
        return bookingList;
    }

    public Optional<Booking> findLastBooking(Long itemId) {
        return bookingRepository.findLastBookings(itemId, LocalDateTime.now())
                .stream().findFirst();
    }

    public Optional<Booking> findNextBooking(Long itemId) {
        return bookingRepository.findNextBookings(itemId, LocalDateTime.now())
                .stream().findFirst();
    }

    private void validateUpdateBookingStatus(User user, Item item, Booking booking) {
        if (!user.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("Пользователь не может изменять статус бронирования");
        }
        if (booking.getStatus().equals(BookingStatus.APPROVED)) {
            throw new ValidationException("Статус бронирования уже установлен APPROVED");
        }
        if (booking.getStatus().equals(BookingStatus.REJECTED)) {
            throw new ValidationException("Статус бронирования уже установлен REJECTED");
        }
    }

    private void validateNewBooking(User user, Item item, Booking booking) {
        if (user.getId().equals(item.getOwner().getId())) {
            throw new NotFoundException("Пользователь не может бронировать свой item");
        }
        if (!item.getAvailable()) {
            throw new ValidationException("Item не доступен для бронирования");
        }
        if (booking.getEnd().isBefore(booking.getStart())) {
            throw new ValidationException("Окончание бронирования не может быть раньше начала бронироваия");
        }
    }
}
