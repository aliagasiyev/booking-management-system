package az.edu.turing.booking_management.controller;

import az.edu.turing.booking_management.model.dto.FlightDto;
import az.edu.turing.booking_management.service.FlightService;

import java.util.List;
import java.util.Optional;

public class FlightController {

    private final FlightService flightService;

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    public Optional<FlightDto> getFlightById(long id) {
        return flightService.getFlightById(id);
    }

    public List<FlightDto> getFlightsByLocationIn24Hours(String location) {

        return flightService.getAllFlightIn24Hours(location);
    }

    public boolean createFlight(FlightDto flightDto) {

        return flightService.createFlight(flightDto);
    }

}