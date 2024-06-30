package az.edu.turing.booking_management.controller;
import az.edu.turing.booking_management.dao.FlightDao;
import az.edu.turing.booking_management.dao.impl.FlightPostgresDao;
import az.edu.turing.booking_management.service.FlightService;
import az.edu.turing.booking_management.service.impl.FlightServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class FlightServlet extends HttpServlet {
    private final FlightServiceHandler flightServiceHandler;
    FlightDao flightDao=new FlightPostgresDao();

    public FlightServlet() {
        FlightService flightService =new FlightServiceImpl(flightDao);
        ObjectMapper objectMapper = new ObjectMapper();
        this.flightServiceHandler = new FlightServiceHandler(flightService,objectMapper);
    }
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        flightServiceHandler.handleCreateFlight(request,response);
    }






}
