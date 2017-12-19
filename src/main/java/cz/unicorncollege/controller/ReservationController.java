package cz.unicorncollege.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import cz.unicorncollege.bt.utils.Choices;

public class ReservationController {
	private MeetingController meetingController;
	private MeetingCentre actualMeetingCentre;
	private MeetingRoom actualMeetingRoom;
	private Date actualDate;

	/**
	 * Constructor for ReservationController class
	 * 
	 * @param meetingController
	 *            loaded data of centers and its rooms
	 */
	public ReservationController(MeetingController meetingController) {
		this.meetingController = meetingController;
		this.actualDate = new Date();
	}

	/**
	 * Method to show options for reservations
	 */
	public void showReservationMenu() {
		List<String> choices = new ArrayList<String>();

		// let them choose one of the loaded meeting centres
		for (MeetingCentre centre : meetingController.getMeetingCentres()) {
			choices.add(centre.getCode() + " - " + centre.getName());
		}

		// get the choice as string to parse it to integer later
		String chosenOption = Choices.getInput("Choose the Meeting Centre: ");
		// get the chosen meeting center
		actualMeetingCentre = meetingController.getMeetingCentres().get(Integer.parseInt(chosenOption));
		choices.clear();

		// display rooms from actual meeting center
		for (MeetingRoom room : actualMeetingCentre.getMeetingRooms()) {
			choices.add(room.getCode() + " - " + room.getName());
		}

		chosenOption = Choices.getInput("Choose the room to create reservation: ");

		actualMeetingRoom = actualMeetingCentre.getMeetingRooms().get(Integer.parseInt(chosenOption));
		choices.clear();

		getItemsToShow();
	}

	private void editReservation() {
		// TODO list reservation as choices, after chosen reservation edit all
		// relevant attributes
	}

	private void addNewReservation() {
		// TODO enter data one by one, add new reservation object to the actual
		// room, than inform about successful adding
	}

	private void deleteReservation() {
		// TODO list all reservations as choices and let enter item for
		// deletion, delete it and inform about successful deletion
	}

	private void changeDate() {
		// TODO let them enter new date in format YYYY-MM-DD, change the actual
		// date, list actual reservations on this date and menu by
		// getItemsToShow()
	}

	private void getItemsToShow() {
		listReservationsByDate(actualDate);

		List<String> choices = new ArrayList<String>();
		choices.add("Edit Reservations");
		choices.add("Add New Reservation");
		choices.add("Delete Reservation");
		choices.add("Change Date");
		choices.add("Exit");

		while (true) {
			switch (Choices.getChoice("Select an option: ", choices)) {
			case 1:
				editReservation();
				break;
			case 2:
				addNewReservation();
				break;
			case 3:
				deleteReservation();
				break;
			case 4:
				changeDate();
				break;
			case 5:
				return;
			}
		}
	}

	private void listReservationsByDate(Date date) {
		// list reservations
		List<Reservation> list = actualMeetingRoom.getSortedReservationsByDate(date);
		if (list != null && list.size() > 0) {
			System.out.println("");
			System.out.println("Reservations for " + getActualData());
			for (Reservation reserv : list) {
				System.out.println(reserv.getFormattedDate());
			}
			System.out.println("");
		} else {
			System.out.println("");
			System.out.println("There are no reservation for " + getActualData());
			System.out.println("");
		}
	}

	/**
	 * Method to get formatted actual date
	 * 
	 * @return
	 */
	private String getFormattedDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-DD");

		return sdf.format(actualDate);
	}

	/**
	 * Method to get actual name of place - meeteng center and room
	 * 
	 * @return
	 */
	private String getCentreAndRoomNames() {
		return "MC: " + actualMeetingCentre.getName() + " , MR: " + actualMeetingRoom.getName();
	}

	/**
	 * Method to get actual state - MC, MR, Date
	 * 
	 * @return
	 */
	private String getActualData() {
		return getCentreAndRoomNames() + ", Date: " + getFormattedDate();
	}
}
