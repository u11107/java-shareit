package ru.practicum.booking;

import ru.practicum.booking.dto.BookingDto;
import ru.practicum.booking.dto.BookingDtoIn;
import ru.practicum.booking.dto.Status;
import ru.practicum.booking.model.Booking;
import ru.practicum.item.dto.ItemInBookingDto;
import ru.practicum.item.model.Item;
import ru.practicum.user.UserMapper;
import ru.practicum.user.model.User;

public class BookingMapper {
    public static Booking createNewModel(BookingDtoIn dto, User booker, Item item) {
        var model = new Booking();
        model.setBooker(booker);
        model.setItem(item);
        model.setStartDateTime(dto.getStartDateTime());
        model.setEndDateTime(dto.getEndDateTime());
        model.setApproved(false);
        model.setCanceled(false);
        return model;
    }

    public static BookingDto toDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStartDateTime(),
                booking.getEndDateTime(),
                new ItemInBookingDto(booking.getItem().getId(), booking.getItem().getName()),
                UserMapper.toUserDto(booking.getBooker()),
                getStatus(booking));
    }

    private static Status getStatus(Booking model) {
        if (model.getCanceled()) {
            return Status.REJECTED;
        } else if (model.getApproved()) {
            return Status.APPROVED;
        }
        return Status.WAITING;
    }
}
