package cz.unicorncollege.bt.model;

import java.util.ArrayList;
import java.util.List;

public class MeetingCentre extends MeetingObject {
	private List<MeetingRoom> meetingRooms = new ArrayList<MeetingRoom>();

	public List<MeetingRoom> getMeetingRooms() {
		return meetingRooms;
	}

	public void addMeetingRoom (MeetingRoom meetingRoom) {
		this.meetingRooms.add(meetingRoom);
	}
	public void removeMeetingRoom (MeetingRoom meetingRoom) {
		this.meetingRooms.remove(meetingRoom);
	}

	public boolean hasCentreReservation () {
		Integer counter = 0;
		for (MeetingRoom room : getMeetingRooms()) {
			counter += room.getReservations().size();
		}

		if (counter > 0) {
			return true;
		} else {
			return false;
		}
	}
}
