package ru.practicum.booking.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.booking.BookingMapper;
import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoIn;
import ru.practicum.booking.dto.Status;
import ru.practicum.booking.service.BookingService;
import ru.practicum.exception.InternalServerErrorException;

import java.util.Collection;


@RestController
@RequestMapping(path = "/bookings")
@AllArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    private BookingDto createNew(@RequestBody BookingDtoIn dto, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        return BookingMapper.toDto(bookingService.createNew(dto, bookerId));
    }

    @PatchMapping("/{bookingId}")
    private BookingDto update(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int ownerId, Boolean approved) {
        return BookingMapper.toDto(bookingService.update(bookingId, ownerId, approved));
    }

    @GetMapping("/{bookingId}")
    private BookingDto get(@PathVariable int bookingId, @RequestHeader("X-Sharer-User-Id") int userId) {
        return BookingMapper.toDto(bookingService.getById(bookingId, userId));
    }

    @GetMapping
    private Collection<BookingDto> get(@RequestParam(defaultValue = "ALL", name = "state") String statusString, @RequestHeader("X-Sharer-User-Id") int bookerId) {
        var status = Status.ALL;
        try {
            status = Status.valueOf(statusString);
        } catch (IllegalArgumentException ex) {
            throw new InternalServerErrorException("Unknown state: " + statusString);
        }
        return bookingService.getAllByBookerAndStatus(bookerId, status);
    }


    @GetMapping("/owner")
    private Collection<BookingDto> getByOwner(@RequestParam(defaultValue = "ALL", name = "state") String statusString, @RequestHeader("X-Sharer-User-Id") int ownerId) {
        var status = Status.ALL;
        try {
            status = Status.valueOf(statusString);
        } catch (IllegalArgumentException ex) {
            throw new InternalServerErrorException("Unknown state: " + statusString);
        }
        return bookingService.getAllByOwnerAndStatus(ownerId, status);
    }
}
