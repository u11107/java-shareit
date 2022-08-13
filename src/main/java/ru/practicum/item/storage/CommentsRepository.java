package ru.practicum.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.item.model.Comment;

public interface CommentsRepository extends JpaRepository<Comment, Integer> {
}
