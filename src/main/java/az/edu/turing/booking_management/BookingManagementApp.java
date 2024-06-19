package az.edu.turing.booking_management;

import az.edu.turing.booking_management.controller.BookingController;
import az.edu.turing.booking_management.controller.FlightController;
import az.edu.turing.booking_management.dao.BookingDao;
import az.edu.turing.booking_management.dao.FlightDao;
import az.edu.turing.booking_management.dao.impl.BookingPostgresDao;
import az.edu.turing.booking_management.dao.impl.FlightPostgresDao;
import az.edu.turing.booking_management.exception.NoEnoughSeatsException;
import az.edu.turing.booking_management.exception.NoSuchReservationException;
import az.edu.turing.booking_management.exception.NotAValidFlightException;
import az.edu.turing.booking_management.model.dto.FlightDto;
import az.edu.turing.booking_management.service.BookingService;
import az.edu.turing.booking_management.service.FlightService;
import az.edu.turing.booking_management.service.impl.BookingServiceImpl;
import az.edu.turing.booking_management.service.impl.FlightServiceImpl;
import az.edu.turing.booking_management.util.DatabaseUtils;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Scanner;

public class BookingManagementApp {
    private static final Scanner scanner = new Scanner(System.in);
    private static final BookingDao bookingDao = new BookingPostgresDao();
    //new BookingPostgresDao(new ObjectMapper().registerModule(new JavaTimeModule()));
    private static final FlightDao flightDao = new FlightPostgresDao();
    //new FlightFileDao(new ObjectMapper().registerModule(new JavaTimeModule()));
    private static final FlightService flightService = new FlightServiceImpl(flightDao);
    private static final FlightController flightController = new FlightController(flightService);
    private static final BookingService bookingService = new BookingServiceImpl(bookingDao, flightDao);
    private static final BookingController bookingController = new BookingController(bookingService);
    private static final DatabaseUtils databaseUtils = new DatabaseUtils();

    public static void main(String[] args) {
        databaseUtils.resetAll();
        FlightDto flight1 = new FlightDto(
                LocalDateTime.of(2024, 6, 3, 10, 30), "Kiev", "Baku", 15);
        FlightDto flight2 = new FlightDto(
                LocalDateTime.of(2024, 6, 3, 8, 30), "Kiev", "Salyan", 13);
        FlightDto flight3 = new FlightDto(
                LocalDateTime.of(2024, 6, 3, 7, 30), "London", "Bilasuvar republic", 2);
        flightController.createFlight(flight1);
        flightController.createFlight(flight2);
        flightController.createFlight(flight3);
        while (true) {
            displayMenu();
            int choice = readIntChoice();
            switch (choice) {
                case 1:
                    try {
                        System.out.println("Enter your location: ");
                        String location = readStringChoice();
                        List<FlightDto> filteredFlights = flightController.getFlightsByLocationIn24Hours(location);
                        if (!filteredFlights.isEmpty()) {
                            filteredFlights.forEach(flightDto -> System.out.println("Flight id: " +
                                    flightDto.getFlight_id() + " || Destination: " +
                                    flightDto.getDestination() + " || Fly time: " +
                                    flightDto.getDeparture_time()));
                        } else {
                            System.out.println("No flights from your destination!");
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Something went wrong! Try again!");
                    }
                case 2:
                    try {
                        System.out.println("Enter flight id: ");
                        int tempChoice = readIntChoice();
                        System.out.println(flightController.getFlightById(tempChoice).get().toString());
                        break;
                    } catch (Exception e) {
                        System.out.println("Something went wrong! Try again!");
                    }
                case 3:
                    try {
                        System.out.println("Enter the count of tickets: ");
                        int count = readIntChoice();
                        String[] passengers = new String[count];
                        for (int i = 0; i < count; i++) {
                            System.out.println("Enter " + i + ". name: ");
                            passengers[i] = scanner.next();
                        }
                        System.out.println("Enter flight id: ");
                        int flightIdForBooking = readIntChoice();
                        bookingController.bookAReservation(passengers, flightIdForBooking);
                        System.out.println("Booking successful");
                        break;
                    } catch (NoEnoughSeatsException | NotAValidFlightException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Something went wrong! Try again!");
                    }
                case 4:
                    try {
                        System.out.println("Enter booking ID for cancellation: ");
                        int flightIdForCancellation = readIntChoice();
                        bookingController.cancelAReservation(flightIdForCancellation);
                        System.out.println("Cancellation successful");
                        break;
                    } catch (NoSuchReservationException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Something went wrong! Try again!");
                    }
                case 5:
                    try {
                        System.out.println("Enter your name: ");
                        String name = readStringChoice();
                        bookingController.getMyReservations(name).forEach(bookingEntity ->
                                System.out.println("Booking ID: " +
                                        bookingEntity.getBookingId() +
                                        " * " +
                                        flightController.getFlightById(bookingEntity
                                                        .getBookingId())
                                                .get()
                                                .toString()));
                        break;
                    } catch (NoSuchReservationException e) {
                        System.out.println(e.getMessage());
                    } catch (Exception e) {
                        System.out.println("Something went wrong! Try again!");
                    }
                case 6:
                    System.out.println("Exiting the program ...");
                    return;
                default:
                    System.out.println("Invalid choice. Please choose again!");
            }
        }
    }


    private static void displayMenu() {
        System.out.println("Menu:");
        System.out.println("1. Online-board.");
        System.out.println("2. Show the flight info.");
        System.out.println("3. Search and book a flight.");
        System.out.println("4. Cancel the booking.");
        System.out.println("5. My flights.");
        System.out.println("6. Exit.");
    }

    private static int readIntChoice() {
        return scanner.nextInt();
    }

    private static String readStringChoice() {
        return scanner.next();
    }
}
