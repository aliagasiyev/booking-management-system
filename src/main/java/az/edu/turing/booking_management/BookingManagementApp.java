package az.edu.turing.booking_management;

import az.edu.turing.booking_management.controller.bookingServletController.BookingCreateServlet;
import az.edu.turing.booking_management.controller.bookingServletController.BookingetReservationServlet;
import az.edu.turing.booking_management.controller.bookingServletController.CancelByIdServlet;
import az.edu.turing.booking_management.controller.flightServletController.FlightByIdServlet;
import az.edu.turing.booking_management.controller.flightServletController.FlightByLocation;
import az.edu.turing.booking_management.controller.flightServletController.FlightCreateServlet;
import az.edu.turing.booking_management.dao.BookingDao;
import az.edu.turing.booking_management.dao.FlightDao;
import az.edu.turing.booking_management.dao.impl.BookingPostgresDao;
import az.edu.turing.booking_management.dao.impl.FlightPostgresDao;
import az.edu.turing.booking_management.service.BookingService;
import az.edu.turing.booking_management.service.FlightService;
import az.edu.turing.booking_management.service.impl.BookingServiceImpl;
import az.edu.turing.booking_management.service.impl.FlightServiceImpl;
import az.edu.turing.booking_management.util.DatabaseUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class BookingManagementApp {
    private static final BookingDao bookingDao = new BookingPostgresDao();
    private static final FlightDao flightDao = new FlightPostgresDao();
    private static final FlightService flightService = new FlightServiceImpl(flightDao);
    private static final BookingService bookingService = new BookingServiceImpl(bookingDao, flightDao);
    private static final DatabaseUtils databaseUtils = new DatabaseUtils();
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    public static void main(String[] args) throws Exception {
        databaseUtils.resetAll();
        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        handler.addServlet(new ServletHolder(new BookingCreateServlet()), "/booking/create");
        handler.addServlet(new ServletHolder(new BookingetReservationServlet()), "/booking/reservation");
        handler.addServlet(new ServletHolder(new CancelByIdServlet()),"/booking/cancel-id");

       handler.addServlet(new ServletHolder(new FlightCreateServlet()),"/flight/create");
       handler.addServlet(new ServletHolder(new FlightByLocation()),"/flight/by-location");
       handler.addServlet(new ServletHolder(new FlightByIdServlet()),"/flight/by-id");

        server.start();
        server.join();
    }
}
