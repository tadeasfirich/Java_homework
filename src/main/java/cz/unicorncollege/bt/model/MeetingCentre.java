package cz.unicorncollege.bt.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import java.util.ArrayList;
import java.util.List;

@Entity
public class MeetingCentre {
	@Column
	private long id;

	@Column
	private List<MeetingRoom> meetingRooms = new ArrayList<MeetingRoom>();

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
