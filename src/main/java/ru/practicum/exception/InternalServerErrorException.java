package ru.practicum.exception;

import lombok.Getter;

@Getter
public class InternalServerErrorException extends RuntimeException {
    private final String message;

    public InternalServerErrorException(String message) {
        this.message = message;
    }
}
