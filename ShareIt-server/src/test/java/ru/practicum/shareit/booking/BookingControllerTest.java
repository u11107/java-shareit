package ru.practicum.shareit.booking;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.controller.BookingController;
import ru.practicum.shareit.booking.dto.BookingDtoNew;
import ru.practicum.shareit.booking.dto.BookingMapper;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingService;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = BookingController.class)
public class BookingControllerTest {

    @MockBean
    private BookingService bookingService;
    @SpyBean
    private BookingMapper bookingMapper;
    @Autowired
    MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    private final Booking newBooking = new Booking(null, LocalDateTime.of(2025, 1, 10, 0, 0),
            LocalDateTime.of(2025, 1, 12, 0, 0), null, null, null);
    private final User user1 = new User(1L, "TestUser1", "test1@mail.com");
    private final Item item1 = new Item(1L, "TestItem1", "TestDescription1", true, user1, null);
    private final Booking booking = new Booking(1L, LocalDateTime.of(2025, 1, 10, 0, 0),
            LocalDateTime.of(2025, 1, 12, 0, 0), item1, user1, BookingStatus.WAITING);

    @Test
    void createBookingTestOk() throws Exception {
        Mockito.when(bookingService.createBooking(Mockito.anyLong(), Mockito.anyLong(), Mockito.any(Booking.class)))
                .thenReturn(booking);
        mockMvc.perform(post("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .content(mapper.writeValueAsString(new BookingDtoNew(1L,
                                LocalDateTime.of(2025, 1, 10, 0, 0),
                                LocalDateTime.of(2025, 1, 12, 0, 0))))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.start").value(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.end").value(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.item").value(booking.getItem()))
                .andExpect(jsonPath("$.booker").value(booking.getBooker()))
                .andExpect(jsonPath("$.status").value(booking.getStatus().toString()));
    }

    @Test
    void getBookingByIdTestOk() throws Exception {
        Mockito.when(bookingService.getBookingById(1L, 1L)).thenReturn(booking);
        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.start").value(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.end").value(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.item").value(booking.getItem()))
                .andExpect(jsonPath("$.booker").value(booking.getBooker()))
                .andExpect(jsonPath("$.status").value(booking.getStatus().toString()));
    }

    @Test
    void getBookingByIdWithIncorrectId() throws Exception {
        Mockito.when(bookingService.getBookingById(1L, 1L)).thenThrow(NotFoundException.class);
        mockMvc.perform(get("/bookings/1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBookingStatusTestOk() throws Exception {
        Mockito.when(bookingService.updateBookingStatus(1L, 1L, true)).thenReturn(booking);
        mockMvc.perform(patch("/bookings/1?approved=true")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(booking.getId()))
                .andExpect(jsonPath("$.start").value(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.end").value(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$.item").value(booking.getItem()))
                .andExpect(jsonPath("$.booker").value(booking.getBooker()))
                .andExpect(jsonPath("$.status").value(booking.getStatus().toString()));
    }

    @Test
    void updateBookingStatusWithIncorrectApproved() throws Exception {
        mockMvc.perform(patch("/bookings/1?approved=WTF")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is5xxServerError());
    }

    @Test
    void getBookingsByUserIdAndStateTestOk() throws Exception {
        Mockito.when(bookingService.getBookingsByUserIdAndState(1L, "WAITING", 1, 1))
                .thenReturn(List.of(booking));
        mockMvc.perform(get("/bookings?state=WAITING&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(booking.getId()))
                .andExpect(jsonPath("$[0].start").value(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].end").value(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].item").value(booking.getItem()))
                .andExpect(jsonPath("$[0].booker").value(booking.getBooker()))
                .andExpect(jsonPath("$[0].status").value(booking.getStatus().toString()));
    }

    @Test
    void getBookingsByUserIdAndStateWithDefaultState() throws Exception {
        Mockito.when(bookingService.getBookingsByUserIdAndState(1L, "ALL", 1, 1))
                .thenReturn(List.of(booking));
        mockMvc.perform(get("/bookings?from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(booking.getId()))
                .andExpect(jsonPath("$[0].start").value(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].end").value(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].item").value(booking.getItem()))
                .andExpect(jsonPath("$[0].booker").value(booking.getBooker()))
                .andExpect(jsonPath("$[0].status").value(booking.getStatus().toString()));
    }

    @Test
    void getBookingsByUserIdAndStateWithDefaultStateAndPagination() throws Exception {
        Mockito.when(bookingService.getBookingsByUserIdAndState(1L, "ALL", 0, 10))
                .thenReturn(List.of(booking));
        mockMvc.perform(get("/bookings")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(booking.getId()))
                .andExpect(jsonPath("$[0].start").value(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].end").value(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].item").value(booking.getItem()))
                .andExpect(jsonPath("$[0].booker").value(booking.getBooker()))
                .andExpect(jsonPath("$[0].status").value(booking.getStatus().toString()));
    }

    @Test
    void getOwnerBookingsTestOk() throws Exception {
        Mockito.when(bookingService.getOwnerBookings(1L, "WAITING", 1, 1))
                .thenReturn(List.of(booking));
        mockMvc.perform(get("/bookings/owner?state=WAITING&from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(booking.getId()))
                .andExpect(jsonPath("$[0].start").value(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].end").value(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].item").value(booking.getItem()))
                .andExpect(jsonPath("$[0].booker").value(booking.getBooker()))
                .andExpect(jsonPath("$[0].status").value(booking.getStatus().toString()));
    }

    @Test
    void getOwnerBookingsWithDefaultState() throws Exception {
        Mockito.when(bookingService.getOwnerBookings(1L, "ALL", 1, 1))
                .thenReturn(List.of(booking));
        mockMvc.perform(get("/bookings/owner?from=1&size=1")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(booking.getId()))
                .andExpect(jsonPath("$[0].start").value(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].end").value(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].item").value(booking.getItem()))
                .andExpect(jsonPath("$[0].booker").value(booking.getBooker()))
                .andExpect(jsonPath("$[0].status").value(booking.getStatus().toString()));
    }

    @Test
    void getOwnerBookingsWithDefaultStateAndPagination() throws Exception {
        Mockito.when(bookingService.getOwnerBookings(1L, "ALL", 0, 10))
                .thenReturn(List.of(booking));
        mockMvc.perform(get("/bookings/owner")
                        .header("X-Sharer-User-Id", 1L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(booking.getId()))
                .andExpect(jsonPath("$[0].start").value(booking.getStart().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].end").value(booking.getEnd().format(DateTimeFormatter.ISO_DATE_TIME)))
                .andExpect(jsonPath("$[0].item").value(booking.getItem()))
                .andExpect(jsonPath("$[0].booker").value(booking.getBooker()))
                .andExpect(jsonPath("$[0].status").value(booking.getStatus().toString()));
    }
}
