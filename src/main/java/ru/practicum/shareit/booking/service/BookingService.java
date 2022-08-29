package ru.practicum.shareit.booking.service;

import ru.practicum.shareit.booking.model.Booking;

import java.util.List;
import java.util.Optional;

public interface BookingService {

    Booking createBooking(Long userId, Long itemId, Booking booking);

    Booking updateBookingStatus(Long userId, Long bookingId, Boolean approved);

    Booking getBookingById(Long userId, Long bookingId);

    List<Booking> getBookingsByUserIdAndState(Long userId, String state, Integer from, Integer size);


    List<Booking> getOwnerBookings(Long userId, String state, Integer from, Integer size);

    Optional<Booking> findLastBooking(Long itemId);

    Optional<Booking> findNextBooking(Long itemId);
}
