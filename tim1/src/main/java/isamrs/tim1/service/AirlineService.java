package isamrs.tim1.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import isamrs.tim1.dto.AirlineDTO;
import isamrs.tim1.dto.MessageDTO;
import isamrs.tim1.model.Airline;
import isamrs.tim1.model.AirlineAdmin;
import isamrs.tim1.model.Location;
import isamrs.tim1.model.PlaneSegment;
import isamrs.tim1.model.Seat;
import isamrs.tim1.repository.AirlineRepository;
import isamrs.tim1.repository.SeatRepository;

@Service
public class AirlineService {
	
	@Autowired
	private AirlineRepository airlineRepository;
	
	@Autowired
	private SeatRepository seatRepository;
	
	public String editProfile(Airline airline, String oldName) {
		Airline airlineToEdit = airlineRepository.findOneByName(oldName);
        if (airlineToEdit == null) {
            return "Airline with given name does not exist.";
        }
        
        String newName = airline.getName();
		if (airlineRepository.findOneByName(newName) != null)
			return "Name is already in use by some other airline.";
		
		if (newName != null) {
			airlineToEdit.setName(newName);
		}
		
		String newDescription = airline.getDescription();
		if (newDescription != null) {
			airlineToEdit.setDescription(newDescription);
		}
		
		Location newLocation = airline.getLocation();
		if (newLocation != null) {
			airlineToEdit.getLocation().setLatitude(airline.getLocation().getLatitude());
			airlineToEdit.getLocation().setLongitude(airline.getLocation().getLongitude());
		}
		
        try {
        	airlineRepository.save(airlineToEdit);
        }
        catch(Exception e) {
        	System.out.println(e.getMessage());
			return "Database error.";
        }
        
        return null;
	}

	public AirlineDTO getAirline(AirlineAdmin admin) {
		return new AirlineDTO(admin.getAirline());
	}

	public MessageDTO saveSeats(String[] savedSeats, AirlineAdmin a) {
		Set<PlaneSegment> planeSegments = a.getAirline().getPlaneSegments();
		for (PlaneSegment p : planeSegments) {
			if (p.getSeats().size() == 0) {
				for (String s : savedSeats) {
					String[] idx = s.split("_");
					if (idx[2].equalsIgnoreCase(p.getSegmentClass().toString().substring(0, 1))) {
						Seat st = new Seat();
						st.setRow(Integer.parseInt(idx[0]));
						st.setColumn(Integer.parseInt(idx[1]));
						st.setPlaneSegment(p);
						p.getSeats().add(st);
					}
				}
			}
		}
		for (String s : savedSeats) {
			String[] idx = s.split("_");
			int row = Integer.parseInt(idx[0]);
			int column = Integer.parseInt(idx[1]);
			for (PlaneSegment p : planeSegments) {
				if (idx[2].equalsIgnoreCase(p.getSegmentClass().toString().substring(0, 1))
						&& (!p.checkSeatExistence(row, column))) {
					Seat st = new Seat();
					st.setRow(row);
					st.setColumn(column);
					st.setPlaneSegment(p);
					p.getSeats().add(st);
				}
			}
		}
		ArrayList<Seat> seatForDelete = new ArrayList<Seat>();
		for (PlaneSegment p : planeSegments) {
			Iterator<Seat> it = p.getSeats().iterator();
			while (it.hasNext()) {
				Seat s = it.next();
				List<String> st = Arrays.asList(savedSeats);
				String ste = s.getRow() + "_" + s.getColumn() + "_" + p.getSegmentClass().toString().toLowerCase().charAt(0);
				if (!(st.contains(ste))) {
					seatForDelete.add(s);
					it.remove();
				}
			}
		}
		a.getAirline().setPlaneSegments(planeSegments);
		airlineRepository.save(a.getAirline());
		for (Seat s : seatForDelete) {
			seatRepository.delete(s);
		}
		return new MessageDTO("Seats saved successfully", "");
	}

}
