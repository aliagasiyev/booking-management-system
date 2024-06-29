package az.edu.turing.booking_management.controller;
import az.edu.turing.booking_management.dao.BookingDao;
import az.edu.turing.booking_management.dao.impl.BookingPostgresDao;
import az.edu.turing.booking_management.service.BookingService;
import az.edu.turing.booking_management.service.impl.BookingServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class BookingServlet extends HttpServlet {
    private final BookingServiceHandler bookingServiceHandler;
    BookingDao bookingDao = new BookingPostgresDao();
    public BookingServlet() {
          BookingService bookingService = new BookingServiceImpl(bookingDao);
        ObjectMapper objectMapper = new ObjectMapper(); // ObjectMapper for JSON handling
        this.bookingServiceHandler = new BookingServiceHandler(bookingService, objectMapper);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        bookingServiceHandler.handleCreateBooking(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        bookingServiceHandler.handleCancelBooking(req, resp);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        bookingServiceHandler.handleGetReservations(req, resp);
    }
}
