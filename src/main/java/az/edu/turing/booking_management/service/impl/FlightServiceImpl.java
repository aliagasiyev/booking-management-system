package az.edu.turing.booking_management.service.impl;

import az.edu.turing.booking_management.dao.FlightDao;
import az.edu.turing.booking_management.model.dto.FlightDto;
import az.edu.turing.booking_management.model.entity.FlightEntity;
import az.edu.turing.booking_management.service.FlightService;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class FlightServiceImpl implements FlightService {
    private final FlightDao flightDao;

    public FlightServiceImpl(FlightDao flightDao) {
        this.flightDao = flightDao;
    }

    @Override
    public List<FlightDto> getAllFlights() {
        List<FlightEntity> allFlights = flightDao.getAll();
        return Optional.ofNullable(allFlights)
                .orElse(Collections.emptyList())
                .stream()
                .map(flight -> new FlightDto(
                        flight.getFlight_id(),
                        flight.getFreeSpaces(),
                        flight.getLocation(),
                        flight.getDestination(),
                        flight.getDeparture_time()))
                .collect(Collectors.toList());
    }

    @Override
    public List<FlightDto> getAllFlightIn24Hours() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextTwentyHours =now.plusHours(24);
        List<FlightEntity> flights = Optional.ofNullable(flightDao.getAll()).orElse(Collections.emptyList());

    }

    @Override
    public Optional<FlightDto> getFlightById(long flightId) {
        return Optional.empty();
    }

    @Override
    public boolean createFlight(FlightDto flightDto) {
        return false;
    }
}
