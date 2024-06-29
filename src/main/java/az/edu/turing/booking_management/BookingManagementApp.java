package az.edu.turing.booking_management;

import az.edu.turing.booking_management.controller.BookingController;
import az.edu.turing.booking_management.controller.BookingServlet;
import az.edu.turing.booking_management.controller.FlightController;
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
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class BookingManagementApp {
    private static final BookingDao bookingDao = new BookingPostgresDao();
    private static final FlightDao flightDao = new FlightPostgresDao();
    private static final FlightService flightService = new FlightServiceImpl(flightDao);
    private static final FlightController flightController = new FlightController(flightService);
    private static final BookingService bookingService = new BookingServiceImpl(bookingDao, flightDao);
    private static final BookingController bookingController = new BookingController(bookingService);
    private static final DatabaseUtils databaseUtils = new DatabaseUtils();

    public static void main(String[] args) throws Exception {
        databaseUtils.resetAll();
        createSampleFlights();

        Server server = new Server(8080);
        ServletContextHandler handler = new ServletContextHandler(ServletContextHandler.SESSIONS);
        handler.setContextPath("/");
        server.setHandler(handler);

        // Pass the controllers to the servlets
        handler.addServlet(new ServletHolder(new BookingServlet(bookingService)), "/bookings/*");
        server.start();
        server.join();
    }

    private static void createSampleFlights() {
        FlightDto flight1 = new FlightDto(LocalDateTime.of(2024, 6, 20, 1, 30), "Kiev", "Baku", 15);
        FlightDto flight2 = new FlightDto(LocalDateTime.of(2024, 6, 20, 2, 30), "Kiev", "Salyan", 13);
        FlightDto flight3 = new FlightDto(LocalDateTime.of(2024, 6, 20, 3, 30), "London", "Bilasuvar republic", 2);
        flightController.createFlight(flight1);
        flightController.createFlight(flight2);
        flightController.createFlight(flight3);
    }
}

