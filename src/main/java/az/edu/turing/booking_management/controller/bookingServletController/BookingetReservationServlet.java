package az.edu.turing.booking_management.controller.bookingServletController;

import az.edu.turing.booking_management.exception.NoSuchReservationException;
import az.edu.turing.booking_management.model.entity.BookingEntity;
import az.edu.turing.booking_management.service.BookingService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

public class BookingetReservationServlet extends HttpServlet {
    private  ObjectMapper objectMapper;
    private  BookingService bookingService;

    public BookingetReservationServlet(ObjectMapper objectMapper, BookingService bookingService) {
        this.objectMapper = objectMapper;
        this.bookingService = bookingService;
    }

    public BookingetReservationServlet() {
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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

