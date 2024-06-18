package az.edu.turing.booking_management.controller;

import az.edu.turing.booking_management.model.entity.BookingEntity;
import az.edu.turing.booking_management.service.BookingService;

import java.util.List;

public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public boolean bookAReservation(String [] passangers, long flightId) {
        return bookingService.bookAReservation(passangers, flightId);
    }

    public void cancelAReservation(long bookingId) {
        bookingService.cancelAReservation(bookingId);
    }

    public List<BookingEntity> getMyReservations(String passengerName) {
        return bookingService.getMyReservations(passengerName);
    }
}
