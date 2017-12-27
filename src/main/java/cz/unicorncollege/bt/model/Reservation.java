package cz.unicorncollege.bt.model;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Reservation {
	private MeetingRoom meetingRoom;
	private Date date;
	private String timeFrom;
	private String timeTo;
	private int expectedPersonCount;
	private String customer;
	private boolean needVideoConference;
	private String note;

	public MeetingRoom getMeetingRoom() {
		return meetingRoom;
	}

	public void setMeetingRoom(MeetingRoom meetingRoom) {
		this.meetingRoom = meetingRoom;
	}

	public Date getDate() {
		return date;
	}

	public String getFormattedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public String getDottedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
		return sdf.format(date);
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getTimeFrom() {
		return timeFrom;
	}

	public void setTimeFrom(String timeFrom) {
		this.timeFrom = timeFrom;
	}

	public String getTimeTo() {
		return timeTo;
	}

	public void setTimeTo(String timeTo) {
		this.timeTo = timeTo;
	}

	public int getExpectedPersonCount() {
		return expectedPersonCount;
	}

	public void setExpectedPersonCount(int expectedPersonCount) {
		this.expectedPersonCount = expectedPersonCount;
	}

	public String getCustomer() {
		return customer;
	}

	public void setCustomer(String customer) {
		this.customer = customer;
	}

	public boolean isNeedVideoConference() {
		return needVideoConference;
	}

	public void setNeedVideoConference(boolean needVideoConference) {
		this.needVideoConference = needVideoConference;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public boolean validateTime(String timeFrom, String timeTo, MeetingRoom room, Date date) {
		timeFrom = timeFrom.replace(":",".");
		timeTo = timeTo.replace(":",".");
		if (!room.getReservations().isEmpty()) {
			for (Reservation reservation : room.getReservations()) {
				if (reservation.getDate().equals(date)) {
					if ((toFloat(timeFrom) <= toFloat(reservation.getTimeFrom())) && (toFloat(reservation.getTimeFrom()) <= toFloat(timeTo))) {
						return false;
					} else if ((toFloat(timeFrom) <= toFloat(reservation.getTimeTo())) && (toFloat(reservation.getTimeTo()) <= toFloat(timeTo))) {
						return false;
					}
				}
			}
		}
		if (toFloat(timeFrom)>= toFloat(timeTo)) {
			return false;
		} else {
			return true;
		}
	}

	public float toFloat (String time) {
		float floatTime = Float.parseFloat(time);
		return floatTime;
	}
}
