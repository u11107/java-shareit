package ru.practicum.requests.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class ItemRequestDto {
    private Long id;
    private String description;
    @JsonProperty("requester_id")
    private Integer requesterId;
    private LocalDate created;
}
