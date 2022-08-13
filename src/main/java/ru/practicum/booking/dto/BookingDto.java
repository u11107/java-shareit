package ru.practicum.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.item.dto.ItemInBookingDto;
import ru.practicum.user.dto.UserDto;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BookingDto {
    private int id;
    @JsonProperty("start")
    private LocalDateTime startDateTime;
    @JsonProperty("end")
    private LocalDateTime endDateTime;
    private ItemInBookingDto item;
    private UserDto booker;
    private Status status;
}
