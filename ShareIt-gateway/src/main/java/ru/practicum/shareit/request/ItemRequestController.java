package ru.practicum.shareit.request;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.request.dto.RequestCreateDto;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;

@RestController
@Validated
@RequiredArgsConstructor
@RequestMapping(path = "/requests")
@Slf4j
public class ItemRequestController {

    private final ItemRequestClient requestClient;

    @GetMapping
    public ResponseEntity<Object> getAllRequestsByOwnerId(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId) {
        log.info("Get all owner requests with ownerId = {}", ownerId);
        return requestClient.getAllRequestsByOwnerId(ownerId);
    }

    @PostMapping
    public ResponseEntity<Object> createItemRequest(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                                    @Valid @RequestBody RequestCreateDto requestCreateDto) {
        log.info("Create Item requests {} with ownerId = {}", requestCreateDto, ownerId);
        return requestClient.createItemRequest(ownerId, requestCreateDto);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequestsByPage(
                                                    @RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                                    @RequestParam(defaultValue = "0") @Min(0) Integer from,
                                                    @RequestParam(defaultValue = "10") @Min(1) Integer size) {
        log.info("Get all requests page for user with id = {}", ownerId);
        return requestClient.getAllRequestsByPage(ownerId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getItemRequestById(@RequestHeader("X-Sharer-User-Id") @Positive Long ownerId,
                                                     @PathVariable @Positive Long requestId) {
        log.info("Get item request with id = {}, by userId = {}", requestId, ownerId);
        return requestClient.getItemRequestById(ownerId, requestId);
    }
}
