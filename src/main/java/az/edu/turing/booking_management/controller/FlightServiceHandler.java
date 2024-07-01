package az.edu.turing.booking_management.controller;
import az.edu.turing.booking_management.dao.FlightDao;
import az.edu.turing.booking_management.dao.impl.FlightPostgresDao;
import az.edu.turing.booking_management.model.dto.FlightDto;
import az.edu.turing.booking_management.service.FlightService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;
import java.util.Optional;


public class FlightServiceHandler {
    private final FlightService flightService;
    private final ObjectMapper objectMapper;

    public FlightServiceHandler(FlightService flightService, ObjectMapper objectMapper) {
        this.flightService = flightService;
        this.objectMapper = objectMapper;
    }

    public void handleGetFlightById(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            long id = Long.parseLong(request.getParameter("id"));
            Optional<FlightDto> flight = flightService.getFlightById(id);
            if (flight.isPresent()) {
                response.getWriter().write(objectMapper.writeValueAsString(flight.get()));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Flight not found");
            }
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    public void handleGetFlightsByLocationIn24Hours(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json");
            String location = req.getParameter("location");
            List<FlightDto> flights = flightService.getAllFlightIn24Hours(location);
            resp.getWriter().write(objectMapper.writeValueAsString(flights));
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }

    public void handleCreateFlight(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            FlightDao flightDao = new FlightPostgresDao();
            resp.setContentType("application/json");
            FlightDto flightDto = objectMapper.readValue(req.getReader(), FlightDto.class);
            boolean creationSuccess = flightService.createFlight(flightDto, flightDao);
            if (creationSuccess) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("Flight creation successful");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Flight creation failed");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error: " + e.getMessage());
        }
    }

}
