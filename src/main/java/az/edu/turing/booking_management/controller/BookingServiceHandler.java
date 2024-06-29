package az.edu.turing.booking_management.controller;
import az.edu.turing.booking_management.exception.NoEnoughSeatsException;
import az.edu.turing.booking_management.exception.NoSuchReservationException;
import az.edu.turing.booking_management.exception.NotAValidFlightException;
import az.edu.turing.booking_management.model.entity.BookingEntity;
import az.edu.turing.booking_management.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class BookingServiceHandler {
    private final BookingService bookingService;
    private final ObjectMapper objectMapper;

    public BookingServiceHandler(BookingService bookingService, ObjectMapper objectMapper) {
        this.bookingService = bookingService;
        this.objectMapper = objectMapper;
    }

    public void handleCreateBooking(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            BookingEntity bookingEntity = objectMapper.readValue(req.getReader(), BookingEntity.class);
            boolean bookingSuccess = bookingService.bookAReservation(bookingEntity.getPassengers(), bookingEntity.getFlightId());
            if (bookingSuccess) {
                resp.setStatus(HttpServletResponse.SC_OK);
                resp.getWriter().write("Booking successful");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Booking failed");
            }
        } catch (NoEnoughSeatsException | NotAValidFlightException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }

    public void handleCancelBooking(HttpServletRequest req, HttpServletResponse resp) throws IOException {
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

    public void handleGetReservations(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            String passengerName = req.getParameter("name");
            List<BookingEntity> reservations = bookingService.getMyReservations(passengerName);
            resp.setContentType("application/json");
            resp.getWriter().write(objectMapper.writeValueAsString(reservations));
        } catch (NoSuchReservationException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }
}
