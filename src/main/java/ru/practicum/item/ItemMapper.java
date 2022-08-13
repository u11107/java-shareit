package ru.practicum.item;

import org.springframework.stereotype.Component;
import ru.practicum.booking.service.BookingService;
import ru.practicum.item.dto.ItemBookingDto;
import ru.practicum.item.dto.ItemDto;
import ru.practicum.item.model.Item;
import ru.practicum.item.service.CommentsService;
import ru.practicum.user.model.User;

import java.util.stream.Collectors;

@Component
public class ItemMapper {
    private final CommentsService commentsService;
    private final BookingService bookingService;

    public ItemMapper(CommentsService commentsService, BookingService bookingService) {
        this.commentsService = commentsService;
        this.bookingService = bookingService;
    }

    public ItemDto toItemDto(Item item) {
        ItemBookingDto lastBooking = bookingService.getLastBooking(item.getId());
        ItemBookingDto nextBooking = bookingService.getNextBooking(item.getId());
        return new ItemDto(
                item.getId(),
                item.getName(),
                item.getDescription(),
                item.getAvailable(),
                item.getOwner().getId(),
                lastBooking,
                nextBooking,
                commentsService.getForItem(item.getId()).stream().map(CommentMapper::toDto).collect(Collectors.toList())
        );
    }

    public static Item toItem(ItemDto itemDto, User owner) {
        return new Item(-1, itemDto.getName(), itemDto.getDescription(), itemDto.getAvailable(),
                owner, null);
    }

    public static Item copyNotEmpty(Item item, ItemDto itemDto) {
        if (itemDto.getAvailable() != null) {
            item.setAvailable(itemDto.getAvailable());
        }
        if (itemDto.getName() != null) {
            item.setName(itemDto.getName());
        }
        if (itemDto.getDescription() != null) {
            item.setDescription(itemDto.getDescription());
        }
        return item;
    }
}
