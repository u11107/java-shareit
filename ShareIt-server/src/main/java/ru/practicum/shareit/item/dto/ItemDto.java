package ru.practicum.shareit.item.dto;

import lombok.*;
import ru.practicum.shareit.booking.model.Booking;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ItemDto {

    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private UserDto owner;
    private Long requestId;
    private BookingDto lastBooking;
    private BookingDto nextBooking;
    private List<CommentDto> comments;

    public void setComments(List<CommentDto> comments) {
        this.comments = comments;
    }

    public void setLastBooking(Booking booking) {
        if (booking != null) {
            this.lastBooking  = new BookingDto(
                    booking.getId(),
                    booking.getBooker().getId(),
                    booking.getStart(),
                    booking.getEnd()
            );
        }
    }

    public void setNextBooking(Booking booking) {
        if (booking != null) {
            this.nextBooking  = new BookingDto(
                    booking.getId(),
                    booking.getBooker().getId(),
                    booking.getStart(),
                    booking.getEnd()
            );
        }
    }

    @Value
    public static class UserDto {
        Long id;
        String name;
    }

    @Value
    static class BookingDto {
        Long id;
        Long bookerId;
        LocalDateTime start;
        LocalDateTime end;
    }
}
