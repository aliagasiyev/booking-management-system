package az.edu.turing.booking_management.controller;

import az.edu.turing.booking_management.exception.NoEnoughSeatsException;
import az.edu.turing.booking_management.exception.NoSuchReservationException;
import az.edu.turing.booking_management.exception.NotAValidFlightException;
import az.edu.turing.booking_management.model.entity.BookingEntity;
import az.edu.turing.booking_management.service.BookingService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class BookingServlet extends HttpServlet {
    private final BookingService bookingService;

    public BookingServlet(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            // Extract parameters from the request
            String[] passengers = req.getParameterValues("passengers");
            long flightId = Long.parseLong(req.getParameter("flightId"));

            // Validate parameters
            if (passengers == null || passengers.length == 0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Passengers parameter is missing or empty");
                return;
            }
            if (flightId <= 0) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid flight ID");
                return;
            }

            // Call the booking service
            boolean bookingSuccess = bookingService.bookAReservation(passengers, flightId);

            // Send success response
            if (bookingSuccess) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Booking successful");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Booking failed");
            }
        } catch (NoEnoughSeatsException | NotAValidFlightException e) {
            // Handle specific exceptions
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            // Handle other exceptions
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            long bookingId = Long.parseLong(req.getParameter("bookingId"));
            bookingService.cancelAReservation(bookingId);
            resp.getWriter().write("Cancellation successful");
        } catch (NoSuchReservationException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            String passengerName = req.getParameter("name");
            List<BookingEntity> reservations = bookingService.getMyReservations(passengerName);
            for (BookingEntity booking : reservations) {
                resp.getWriter().write("Booking ID: " + booking.getBookingId() + "\n");
            }
        } catch (NoSuchReservationException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }
}
