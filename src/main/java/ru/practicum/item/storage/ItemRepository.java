package ru.practicum.item.storage;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.item.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer> {
}
