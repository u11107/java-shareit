package ru.practicum.shareit.user.mapper;

import org.springframework.stereotype.Component;
import ru.practicum.shareit.user.dto.UserDto;
import ru.practicum.shareit.user.model.User;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {
    public UserDto toUserDto(User user) {
        return new UserDto(
            user.getId(),
            user.getName(),
            user.getEmail()
        );
    }

    public List<UserDto> toUserDtoList(List<User> users) {
        var listDto = new ArrayList<UserDto>();
        if (users.isEmpty()) {
            return listDto;
        }
        for (User user : users) {
            UserDto dto = toUserDto(user);
            listDto.add(dto);
        }
        return listDto;
    }
}
