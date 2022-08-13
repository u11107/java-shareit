package ru.practicum.item.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.practicum.booking.service.BookingService;
import ru.practicum.exception.ValidationException;
import ru.practicum.item.CommentMapper;
import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.item.storage.CommentsRepository;
import ru.practicum.user.storage.UserRepository;

import java.util.Collection;
import java.util.stream.Collectors;

@Component
public class CommentsService {
    private final CommentsRepository commentsRepository;
    private final UserRepository userRepository;
    private BookingService bookingService;


    public CommentsService(CommentsRepository commentsRepository, UserRepository userRepository,
                           BookingService bookingService) {
        this.commentsRepository = commentsRepository;
        this.userRepository = userRepository;
        this.bookingService = bookingService;
    }

    public Comment addNew(Item item, CommentDto dto, int authorId) {

        var model = CommentMapper.toModel(dto, item, userRepository.getReferenceById(authorId));
        validate(model);
        return commentsRepository.saveAndFlush(model);
    }

    private void validate(Comment model) {
        if (model.getText() == null || model.getText().trim().length() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
        var bookings = bookingService.getAllStartedByBookerAndItem(model.getAuthor().getId(),
                model.getItem().getId(), true, false);
        if (bookings.size() == 0) {
            throw new ValidationException(HttpStatus.BAD_REQUEST, "");
        }
    }

    public Collection<Comment> getForItem(int itemId) {
        return commentsRepository
                .findAll()
                .stream()
                .filter(c -> c.getItem().getId() == itemId)
                .collect(Collectors.toList());
    }
}
