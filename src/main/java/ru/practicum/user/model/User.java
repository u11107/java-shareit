package ru.practicum.user.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private String email;

    public User fillEmpty(User u) {
        if (id == 0) {
            id = u.id;
        }
        if (name == null) {
            name = u.getName();
        }
        if (email == null) {
            email = u.getEmail();
        }
        return this;
    }
}
