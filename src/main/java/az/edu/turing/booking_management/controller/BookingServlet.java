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
        ObjectMapper objectMapper = new ObjectMapper();
        this.bookingServiceHandler = new BookingServiceHandler(bookingService, objectMapper);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        bookingServiceHandler.handleCreateBooking(request, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        bookingServiceHandler.handleCancelBooking(request, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        bookingServiceHandler.handleGetReservations(request, response);
    }
}
