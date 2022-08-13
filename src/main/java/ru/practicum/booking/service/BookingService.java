package ru.practicum.booking.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.booking.BookingMapper;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoIn;
import ru.practicum.booking.dto.Status;
import ru.practicum.booking.model.Booking;
import ru.practicum.booking.strorage.BookingRepository;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.dto.ItemBookingDto;
import ru.practicum.item.storage.ItemRepository;
import ru.practicum.user.storage.UserRepository;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class BookingService {
    private final BookingRepository bookingRepository;
    private final ItemRepository itemRepository;
    private final UserRepository userRepository;

    public BookingService(BookingRepository bookingRepository, ItemRepository itemRepository, UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.itemRepository = itemRepository;
        this.userRepository = userRepository;
    }

    public Booking createNew(BookingDtoIn dto, int bookerId) {
        validate(dto, bookerId);
        var item = itemRepository.getReferenceById(dto.getItemId());
        if (item.getOwner().getId() == bookerId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        if (!item.getAvailable()) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
        return
                bookingRepository.saveAndFlush(
                        BookingMapper.createNewModel(
                                dto,
                                userRepository.getReferenceById(bookerId),
                                item));
    }

    private void validate(BookingDtoIn dto, int bookerId) {
        if (!itemRepository.existsById(dto.getItemId())) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        if (!userRepository.existsById(bookerId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        if (dto.getStartDateTime().isBefore(LocalDateTime.now()) || dto.getEndDateTime().isBefore(LocalDateTime.now())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
        if (dto.getEndDateTime().isBefore(dto.getStartDateTime())) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
    }

    public Booking update(int bookingId, int ownerId, Boolean approved) {
        if (approved == null) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
        if (!userRepository.existsById(ownerId)) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
        var booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getItem().getOwner().getId() != ownerId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        if (booking.getApproved()) {
            if (approved) {
                throw new ValidationException(HttpStatus.BAD_REQUEST, "");
            }
        }
        booking.setCanceled(!approved);
        booking.setApproved(approved);
        return bookingRepository.saveAndFlush(booking);
    }

    public Booking getById(int bookingId, int userId) {
        if (!bookingRepository.existsById(bookingId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        var booking = bookingRepository.getReferenceById(bookingId);
        if (booking.getBooker().getId() != userId && booking.getItem().getOwner().getId() != userId) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        return booking;
    }

    public Collection<Booking> getAllByBooker(int bookerId) {
        return bookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getBooker().getId() == bookerId)
                .sorted((a, b) -> b.getStartDateTime().compareTo(a.getStartDateTime()))
                .collect(Collectors.toList());
    }

    public Collection<Booking> getAllByOwner(int ownerId) {

        return bookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getItem().getOwner().getId() == ownerId)
                .sorted((a, b) -> b.getStartDateTime().compareTo(a.getStartDateTime()))
                .collect(Collectors.toList());
    }

    public ItemBookingDto getLastBooking(int itemId) {
        return bookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getItem().getId() == itemId)
                .filter(b -> b.getEndDateTime().isBefore(LocalDateTime.now()))
                .max((a, b) -> a.getEndDateTime().compareTo(b.getEndDateTime()))
                .map(b -> new ItemBookingDto(b.getId(), b.getBooker().getId()))
                .orElse(null);
    }

    public ItemBookingDto getNextBooking(int itemId) {
        return bookingRepository
                .findAll()
                .stream()
                .filter(b -> b.getItem().getId() == itemId)
                .filter(b -> b.getStartDateTime().isAfter(LocalDateTime.now()))
                .min((a, b) -> a.getStartDateTime().compareTo(b.getStartDateTime()))
                .map(b -> new ItemBookingDto(b.getId(), b.getBooker().getId()))
                .orElse(null);
    }


    public Collection<Booking> getAllStartedByBookerAndItem(int bookerId, int itemId, boolean approved, boolean cancelled) {
        return getAllByBooker(bookerId)
                .stream()
                .filter(b -> b.getItem().getId() == itemId)
                .filter(b -> b.getApproved() == approved)
                .filter(b -> b.getCanceled() == cancelled)
                .filter(b -> b.getStartDateTime().isBefore(LocalDateTime.now()))
                .collect(Collectors.toList());
    }


    public Collection<BookingDto> getAllByBookerAndStatus(int bookerId, Status status) {
        if (!bookingRepository.existsById(bookerId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        return getAllByBooker(bookerId)
                .stream()
                .map(BookingMapper::toDto)
                .filter(x -> filterByStatus(x, status))
                .collect(Collectors.toList());
    }


    private boolean filterByStatus(BookingDto x, Status status) {
        if (status == Status.ALL) {
            return true;
        }
        if (status == Status.APPROVED || status == Status.REJECTED || status == Status.WAITING) {
            return x.getStatus() == status;
        }

        if (status == Status.PAST) {
            return x.getEndDateTime().isBefore(LocalDateTime.now());
        }
        if (status == Status.FUTURE) {
            return x.getStartDateTime().isAfter(LocalDateTime.now());
        }
        if (status == Status.CURRENT) {
            return x.getEndDateTime().isAfter(LocalDateTime.now()) && x.getStartDateTime().isBefore(LocalDateTime.now());
        }

        throw new ValidationException(HttpStatus.INTERNAL_SERVER_ERROR, "unexpected status");
    }

    public Collection<BookingDto> getAllByOwnerAndStatus(int ownerId, Status status) {
        if (!userRepository.existsById(ownerId)) {
            throw new ValidationException(HttpStatus.NOT_FOUND, "");
        }
        return getAllByOwner(ownerId)
                .stream()
                .map(BookingMapper::toDto)
                .filter(x -> filterByStatus(x, status))
                .collect(Collectors.toList());
    }
}
