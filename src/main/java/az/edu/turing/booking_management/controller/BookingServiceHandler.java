package az.edu.turing.booking_management.controller;

import az.edu.turing.booking_management.dao.BookingDao;
import az.edu.turing.booking_management.dao.FlightDao;
import az.edu.turing.booking_management.dao.impl.BookingPostgresDao;
import az.edu.turing.booking_management.dao.impl.FlightPostgresDao;
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

    public void handleCreateBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            BookingDao bookingDao = new BookingPostgresDao();
            FlightDao flightDao = new FlightPostgresDao();
            response.setContentType("application/json");
            BookingEntity bookingEntity = objectMapper.readValue(request.getReader(), BookingEntity.class);
            boolean bookingSuccess = bookingService.bookAReservation(bookingEntity.getPassengers(), bookingEntity.getFlightId(), bookingDao, flightDao);
            if (bookingSuccess) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Booking Successful...");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Booking Failed");
            }
        } catch (NoEnoughSeatsException | NotAValidFlightException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request: " + e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }

    public void handleCancelBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/json");
            BookingDao bookingDao = new BookingPostgresDao();
            FlightDao flightDao = new FlightPostgresDao();
            long bookingId = Long.parseLong(request.getParameter("bookingId"));
            boolean cancellingSuccess = bookingService.cancelAReservation(bookingId, bookingDao, flightDao);
            if (cancellingSuccess) {
                response.setStatus(HttpServletResponse.SC_NO_CONTENT);
                response.getWriter().write("Booking successfully canceled");
            }
            else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } catch (NoSuchReservationException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Reservation not found: " + e.getMessage());
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Invalid bookingId format: " + e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }

    public void handleGetReservations(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/json");
            String passengerName = request.getParameter("name");
            List<BookingEntity> reservations = bookingService.getMyReservations(passengerName);
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(objectMapper.writeValueAsString(reservations));
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (NoSuchReservationException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Reservations not found: " + e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }
}
