package ru.practicum.item.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.requests.model.ItemRequest;
import ru.practicum.user.model.User;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "items")
@AllArgsConstructor
public class Item {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private Boolean available;
    @OneToOne
    @JoinColumn(name = "owner_id")
    private User owner;
    @OneToOne
    @JoinColumn(name = "request_id")
    private ItemRequest request;

    public Item fillEmpty(Item i) {
        if (id == 0) {
            id = i.id;
        }
        if (getName() == null) {
            name = i.name;
        }
        if (getDescription() == null) {
            description = i.description;
        }
        if (getAvailable() == null) {
            available = i.available;
        }
        if (getOwner() == null) {
            owner = i.owner;
        }
        if (getRequest() == null) {
            request = i.request;
        }
        return this;
    }
}
