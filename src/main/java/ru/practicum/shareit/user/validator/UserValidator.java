package ru.practicum.shareit.user.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.user.model.User;

@Component
@Slf4j
public class UserValidator {
    public void validateUser(User user) throws ValidationException {
        if (user.getEmail() == null || user.getEmail().isEmpty()) {
            log.warn("e-mail не может быть пустым");
            throw new ValidationException("Ошибка валидации, пустое поле email");
        }
        if (!user.getEmail().contains("@")) {
            log.warn("e-mail должен содержать знак @");
            throw new ValidationException("Ошибка валидации, поле должно содержать @");
        }
        if (user.getName() == null || user.getName().isEmpty()) {
            log.warn("Имя не может быть пустым");
            throw new ValidationException("Ошибка валидации, имя не может быть пустым");
        }
    }
}
