package ru.practicum.user;

import org.springframework.stereotype.Component;
import ru.practicum.user.dto.UserDto;
import ru.practicum.user.model.User;

@Component
public class UserMapper {
    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(UserDto user) {
        return new User(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }
}
