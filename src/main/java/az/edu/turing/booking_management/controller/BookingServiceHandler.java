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

    public void handleCreateBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            BookingEntity bookingEntity = objectMapper.readValue(request.getReader(), BookingEntity.class);
            boolean bookingSuccess = bookingService.bookAReservation(bookingEntity.getPassengers(), bookingEntity.getFlightId());
            if (bookingSuccess) {
                response.setStatus(HttpServletResponse.SC_OK);
                response.getWriter().write("Booking Successful...");
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Booking Failed");
            }
        } catch (NoEnoughSeatsException | NotAValidFlightException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Bad request is here");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong please try again");
        }
    }

    public void handleCancelBooking(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            long bookingId = Long.parseLong(request.getParameter("bookingId"));
            bookingService.cancelAReservation(bookingId);
            response.setStatus(HttpServletResponse.SC_NO_CONTENT); //successful
            response.getWriter().write("Booking successful canceled");
        }catch (NoSuchReservationException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }
    public void handleGetReservations(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String passengerName = request.getParameter("name");
            List<BookingEntity> reservations = bookingService.getMyReservations(passengerName);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.setStatus(HttpServletResponse.SC_OK);
            response.getWriter().write(objectMapper.writeValueAsString(reservations));


        } catch (NoSuchReservationException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());

        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }
}
