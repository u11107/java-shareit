package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.item.model.Comment;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentMapper {

    public static CommentDto toCommentDto(Comment comment) {
        return  new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getAuthor().getName(),
                comment.getCreated()
        );
    }

    public static Comment toComment(CommentDto commentDto) {
        return new Comment(
                null,
                commentDto.getText(),
                null,
                null,
                LocalDateTime.now()
        );
    }
}
