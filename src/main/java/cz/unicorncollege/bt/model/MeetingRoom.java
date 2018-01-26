package cz.unicorncollege.bt.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Entity
public class MeetingRoom {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column
	private int capacity;

	@Column
	private boolean hasVideoConference;

	@ManyToOne(cascade={javax.persistence.CascadeType.PERSIST, javax.persistence.CascadeType.REMOVE})
	@JoinColumn
	private MeetingCentre meetingCentre;

	@Column
	private List<Reservation> reservations = new ArrayList<Reservation>();

	@Column
	private String name;

	@Column
	private String code;

	@Column
	private String description;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

	public boolean isHasVideoConference() {
		return hasVideoConference;
	}

	public void setHasVideoConference(boolean hasVideoConference) {
		this.hasVideoConference = hasVideoConference;
	}

	public MeetingCentre getMeetingCentre() {
		return meetingCentre;
	}

	public void setMeetingCentre(MeetingCentre meetingCentre) {
		this.meetingCentre = meetingCentre;
	}

	public List<Reservation> getReservations() {
		return reservations;
	}

	public HashMap<String, List<Reservation>> getReservationsByDate() {
		HashMap<String, List<Reservation>> hashMap = new HashMap<>();
		for (Reservation reservation : reservations) {
			if (!hashMap.containsKey(reservation.getDottedDate())) {
				List<Reservation> listOfReservations = new ArrayList<>();
				listOfReservations.add(reservation);
				hashMap.put(reservation.getDottedDate(), listOfReservations);
			} else {
				hashMap.get(reservation.getDottedDate()).add(reservation);
			}
		}
		return hashMap;
	}

	public List<Reservation> getSortedReservationsByDate(Date getSortedReservationsByDate) {
		//TODO get reservations by date and return sorted reservations by hours
		return null;
	}

	public void addReservation (Reservation reservation) {
		this.reservations.add(reservation);
	}
	public void removeReservation (Reservation reservation) {
		this.reservations.remove(reservation);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
