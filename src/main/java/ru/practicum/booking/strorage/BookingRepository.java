package ru.practicum.booking.strorage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.booking.model.Booking;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
}
