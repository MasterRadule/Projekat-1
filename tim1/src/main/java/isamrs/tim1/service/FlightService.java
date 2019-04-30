package isamrs.tim1.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import isamrs.tim1.dto.FlightDTO;
import isamrs.tim1.dto.FlightUserViewDTO;
import isamrs.tim1.dto.MessageDTO;
import isamrs.tim1.dto.MessageDTO.ToasterType;
import isamrs.tim1.model.Airline;
import isamrs.tim1.model.Flight;
import isamrs.tim1.repository.DestinationRepository;
import isamrs.tim1.repository.FlightRepository;
import isamrs.tim1.repository.ServiceRepository;

@Service
public class FlightService {

	@Autowired
	ServiceRepository serviceRepository;
	
	@Autowired
	DestinationRepository destinationRepository;
	
	@Autowired
	FlightRepository flightRepository;
	
	public ResponseEntity<MessageDTO> addFlight(FlightDTO flightDTO) {
		Airline a = (Airline) serviceRepository.findOneByName(flightDTO.getAirlineName());
		if (a == null)
			return new ResponseEntity<MessageDTO>(new MessageDTO("Airline does not exist.", ToasterType.ERROR.toString()), HttpStatus.BAD_REQUEST);
		Flight flight = new Flight();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
		try {
			flight.setDepartureTime(sdf.parse(flightDTO.getDepartureTime()));
			flight.setLandingTime(sdf.parse(flightDTO.getLandingTime()));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		flight.setStartDestination(destinationRepository.findOneByName(flightDTO.getStartDestination()));
		flight.setEndDestination(destinationRepository.findOneByName(flightDTO.getEndDestination()));
		flight.setFlightLength(flightDTO.getFlightDistance());
		long diffInMillies = Math.abs(flight.getDepartureTime().getTime() - flight.getLandingTime().getTime());
		int flightDuration = (int) TimeUnit.MINUTES.convert(diffInMillies, TimeUnit.MILLISECONDS);
		flight.setFlightDuration(flightDuration);
		flight.setFirstClassPrice(flightDTO.getFirstClassPrice());
		flight.setBusinessClassPrice(flightDTO.getBusinessClassPrice());
		flight.setEconomyClassPrice(flightDTO.getEconomyClassPrice());
		flight.setPricePerBag(flightDTO.getPricePerBag());
		flight.setNumberOfFlightConnections(flightDTO.getConnections().length);
		flight.setLocationsOfConnecting(new ArrayList<String>(Arrays.asList(flightDTO.getConnections())));
		flight.setAverageGrade(0.0);
		flight.setAirline(a);
		a.getFlights().add(flight);
		flightRepository.save(flight);
		return new ResponseEntity<MessageDTO>(new MessageDTO("Flight added successfully.", ToasterType.SUCCESS.toString()), HttpStatus.OK);
	}

	public ArrayList<FlightUserViewDTO> searchFlights(FlightDTO flight) {
		Long startID = destinationRepository.findOneByName(flight.getStartDestination()).getId();
		Long endID = destinationRepository.findOneByName(flight.getEndDestination()).getId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Set<Flight> flights = flightRepository.searchFlightsByDestinations(startID, endID);
		ArrayList<FlightUserViewDTO> flightsList = new ArrayList<FlightUserViewDTO>();
		for (Flight f : flights) {
			if (flight.getDepartureTime().equals(sdf.format(f.getDepartureTime())) && 
					flight.getLandingTime().equals(sdf.format(f.getLandingTime()))) {
				flightsList.add(new FlightUserViewDTO(f));
			}
		}
		return flightsList;
	}

}
