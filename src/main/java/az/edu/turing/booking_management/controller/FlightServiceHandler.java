package az.edu.turing.booking_management.controller;
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

    public void handleGetFlightById(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        try {
            resp.setContentType("application/json");
            long id = Long.parseLong(req.getParameter("id"));
            Optional<FlightDto> flight = flightService.getFlightById(id);
            if (flight.isPresent()) {
                resp.getWriter().write(objectMapper.writeValueAsString(flight.get()));
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Flight not found");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
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
            resp.setContentType("application/json");
            FlightDto flightDto = objectMapper.readValue(req.getReader(), FlightDto.class);
            boolean creationSuccess = flightService.createFlight(flightDto);
            if (creationSuccess) {
                resp.setStatus(HttpServletResponse.SC_CREATED);
                resp.getWriter().write("Flight creation successful");
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Flight creation failed");
            }
        } catch (Exception e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Something went wrong! Try again!");
        }
    }
}
