package ru.practicum.item;

import ru.practicum.item.dto.CommentDto;
import ru.practicum.item.model.Comment;
import ru.practicum.item.model.Item;
import ru.practicum.user.model.User;

import java.time.LocalDateTime;

public class CommentMapper {
    public static CommentDto toDto(Comment model) {
        return new CommentDto(model.getId(), model.getAuthor().getName(), model.getText(), model.getCreated());
    }

    public static Comment toModel(CommentDto dto, Item item, User author) {
        var model = new Comment();
        model.setCreated(LocalDateTime.now());
        model.setText(dto.getText());
        model.setItem(item);
        model.setAuthor(author);
        return model;
    }
}
