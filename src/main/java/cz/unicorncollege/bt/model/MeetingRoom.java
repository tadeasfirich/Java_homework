package cz.unicorncollege.bt.model;

import java.util.ArrayList;
import java.util.Date;
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
}