package az.edu.turing.booking_management.service;

import az.edu.turing.booking_management.model.entity.BookingEntity;

import java.util.List;

public interface BookingService {
    boolean bookAReservation(String [] passengers, long flightId);

    void cancelAReservation(long bookingId);

    List<BookingEntity> getMyReservations(String passengerName);
}
