package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.booking.dto.BookItemRequestDto;
import ru.practicum.shareit.booking.dto.BookingState;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@RestController
@RequestMapping(path = "/bookings")
@RequiredArgsConstructor
@Validated
@Slf4j
public class BookingController {

    private final BookingClient bookingClient;

    @PostMapping
    public ResponseEntity<Object> createBooking(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                @RequestBody @Valid BookItemRequestDto bookingDto) {
        log.info("Create booking {} by user id = {}", bookingDto, userId);
        return bookingClient.createBooking(userId, bookingDto);
    }

    @PatchMapping("/{bookingId}")
    public ResponseEntity<Object> updateBookingStatus(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                          @PathVariable @Positive Long bookingId,
                                          @RequestParam Boolean approved) {
        log.info("Update booking id = {} by user id = {} with approved = {}", bookingId, userId, approved);
        return bookingClient.updateBookingStatus(userId, bookingId, approved);
    }

    @GetMapping("/{bookingId}")
    public ResponseEntity<Object> getBookingById(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                     @PathVariable @Positive Long bookingId) {
        log.info("Get booking id = {} by user id = {}", bookingId, userId);
        return bookingClient.getBookingById(userId, bookingId);
    }

    @GetMapping
    public ResponseEntity<Object> getBookingsByUserIdAndState(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                                        @RequestParam(defaultValue = "ALL") String state,
                                                        @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                        @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        BookingState stateValid = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Get all bookings with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getBookingsByUserIdAndState(userId, stateValid, from, size);
    }

    @GetMapping("/owner")
    public ResponseEntity<Object> getOwnerBookings(@RequestHeader("X-Sharer-User-Id") @Positive Long userId,
                                             @RequestParam(defaultValue = "ALL") String state,
                                             @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                             @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        BookingState stateValid = BookingState.from(state)
                .orElseThrow(() -> new IllegalArgumentException("Unknown state: " + state));
        log.info("Get owner bookings with state {}, userId={}, from={}, size={}", state, userId, from, size);
        return bookingClient.getOwnerBookings(userId, stateValid, from, size);
    }
}
