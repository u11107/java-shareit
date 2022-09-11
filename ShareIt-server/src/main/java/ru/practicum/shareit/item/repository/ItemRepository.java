package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.item.model.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {

    List<Item> findByOwnerId(Long ownerId, Pageable pageable);

    @Query("SELECT i FROM Item AS i " +
            "WHERE i.available = true AND " +
            "(upper(i.name) like upper(concat('%', ?1, '%')) " +
            " or upper(i.description) like upper(concat('%', ?1, '%')))")
    List<Item> searchItemsByTextInNameAndDescription(String text, Pageable pageable);

    List<Item> findItemsByRequestId(Long requestId);
}
