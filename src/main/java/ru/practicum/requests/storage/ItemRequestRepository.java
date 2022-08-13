package ru.practicum.requests.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.requests.model.ItemRequest;

public interface ItemRequestRepository extends JpaRepository<ItemRequest, Integer> {
}
