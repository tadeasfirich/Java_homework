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
}
