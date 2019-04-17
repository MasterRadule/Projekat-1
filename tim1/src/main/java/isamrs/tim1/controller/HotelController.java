package isamrs.tim1.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import isamrs.tim1.dto.DetailedServiceDTO;
import isamrs.tim1.dto.HotelDTO;
import isamrs.tim1.dto.ServiceDTO;
import isamrs.tim1.model.Hotel;
import isamrs.tim1.model.HotelAdmin;
import isamrs.tim1.service.HotelService;

@RestController
public class HotelController {

	@Autowired
	private HotelService hotelService;

	@PreAuthorize("hasRole('HOTELADMIN')")
	@RequestMapping(value = "/api/addHotel", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> addHotel(@RequestBody Hotel hotel) {
		return new ResponseEntity<String>(hotelService.addHotel(hotel), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('HOTELADMIN')")
	@RequestMapping(value = "/api/editHotel", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<String> editHotel(@RequestBody Hotel hotel, @RequestParam(required = true) String oldName) {
		return new ResponseEntity<String>(hotelService.editHotel(hotel, oldName), HttpStatus.OK);
	}

	@PreAuthorize("hasRole('HOTELADMIN')")
	@RequestMapping(
			value = "/api/getHotelOfAdmin",
			method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<HotelDTO> getHotel() {
		return new ResponseEntity<HotelDTO>(
				hotelService
						.getHotel((HotelAdmin) SecurityContextHolder.getContext().getAuthentication().getPrincipal()),
				HttpStatus.OK);
	}
	
	@PreAuthorize("hasRole('SYSADMIN')")
	@RequestMapping(value = "/api/getHotels", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<ArrayList<ServiceDTO>> getHotels() {
		return new ResponseEntity<ArrayList<ServiceDTO>>(
				hotelService.getHotels(),
				HttpStatus.OK);
	}
	

	@PreAuthorize("hasRole('SYSADMIN')")
	@RequestMapping(value = "/api/getHotel", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<DetailedServiceDTO> getHotel(@RequestParam String name) {
		return new ResponseEntity<DetailedServiceDTO>(
				hotelService.getHotel(name),
				HttpStatus.OK);
	}
}
