package az.edu.turing.booking_management.controller;

import az.edu.turing.booking_management.dao.BookingDao;
import az.edu.turing.booking_management.dao.FlightDao;
import az.edu.turing.booking_management.model.entity.BookingEntity;
import az.edu.turing.booking_management.service.BookingService;

import java.util.List;

public class BookingController {
    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    public boolean bookAReservation(String [] passangers, long flightId, BookingDao bookingDao, FlightDao flightDao) {
        return bookingService.bookAReservation(passangers, flightId, bookingDao, flightDao);
    }

    public void cancelAReservation(long bookingId, BookingDao bookingDao, FlightDao flightDao) {
        bookingService.cancelAReservation(bookingId, bookingDao, flightDao);
    }

    public List<BookingEntity> getMyReservations(String passengerName) {
        return bookingService.getMyReservations(passengerName);
    }
}
