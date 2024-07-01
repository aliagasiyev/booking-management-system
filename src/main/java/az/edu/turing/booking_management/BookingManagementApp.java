package az.edu.turing.booking_management;

import az.edu.turing.booking_management.controller.BookingServlet;
import az.edu.turing.booking_management.controller.FlightServlet;
import az.edu.turing.booking_management.dao.BookingDao;
import az.edu.turing.booking_management.dao.FlightDao;
import az.edu.turing.booking_management.dao.impl.BookingPostgresDao;
import az.edu.turing.booking_management.dao.impl.FlightPostgresDao;
import az.edu.turing.booking_management.model.dto.FlightDto;
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
import java.time.LocalDateTime;

public class BookingManagementApp {
    private static final BookingDao bookingDao = new BookingPostgresDao();
    private static final FlightDao flightDao = new FlightPostgresDao();
    private static final FlightService flightService = new FlightServiceImpl(flightDao);
    private static final BookingService bookingService = new BookingServiceImpl(bookingDao, flightDao);
    private static final DatabaseUtils databaseUtils = new DatabaseUtils();
    private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());



    public static void main(String[] args) throws Exception {
        databaseUtils.resetAll();
        createSampleFlights();

        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        handler.addServlet(new ServletHolder(new BookingServlet()), "/bookings/*");
        handler.addServlet(new ServletHolder(new FlightServlet(objectMapper)), "/flights/*");

        server.start();
        server.join();
    }

    private static void createSampleFlights() {
        FlightDto flight1 = new FlightDto(LocalDateTime.of(2024, 6, 20, 1, 30), "Kiev", "Baku", 15);
        FlightDto flight2 = new FlightDto(LocalDateTime.of(2024, 6, 20, 2, 30), "Kiev", "Salyan", 13);
        FlightDto flight3 = new FlightDto(LocalDateTime.of(2024, 6, 20, 3, 30), "London", "Bilasuvar Republic", 2);

        flightService.createFlight(flight1,flightDao);
        flightService.createFlight(flight2,flightDao);
        flightService.createFlight(flight3,flightDao);
    }
}
