package cz.unicorncollege.bt.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class MeetingRoom extends MeetingObject {
	private int capacity;
	private boolean hasVideoConference;
	private MeetingCentre meetingCentre;
	private List<Reservation> reservations = new ArrayList<Reservation>();

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

	//TODO: Smazat
	public boolean hasRoomReservation(MeetingRoom room) {
		if (room.getReservations().size() > 0) {
			return true;
		} else {
			return false;
		}
	}
}
