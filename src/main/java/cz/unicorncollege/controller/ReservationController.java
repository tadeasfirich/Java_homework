package cz.unicorncollege.controller;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import cz.unicorncollege.bt.utils.Choices;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReservationController {
	private MeetingController meetingController;
	public MeetingCentre actualMeetingCentre;
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
		System.out.println();
		System.out.println("Meeting centres:");
		// let them choose one of the loaded meeting centres
		int counter = 0;
		for (MeetingCentre centre : meetingController.getMeetingCentres()) {
			choices.add(centre.getCode() + " - " + centre.getName());
			System.out.print(counter + ": ");
			System.out.print(centre.getName() + ", ");
			System.out.print(centre.getCode() + ", ");
			System.out.print(centre.getDescription());
			System.out.println();
			counter += 1;
		}

		// get the choice as string to parse it to integer later
		String chosenOption = Choices.getInput("Choose the Meeting Centre: ");
		// get the chosen meeting center
		actualMeetingCentre = meetingController.getMeetingCentres().get(Integer.parseInt(chosenOption));
		choices.clear();

		// display rooms from actual meeting center
		if (!actualMeetingCentre.getMeetingRooms().isEmpty()) {
			counter = 0;
			for (MeetingRoom room : actualMeetingCentre.getMeetingRooms()) {
				choices.add(room.getCode() + " - " + room.getName());
				System.out.print(counter + ": ");
				System.out.println(room.getName() + ", " + room.getCode() + ", " + room.getDescription());
				counter += 1;
			}

			chosenOption = Choices.getInput("Choose the room to create reservation: ");

			actualMeetingRoom = actualMeetingCentre.getMeetingRooms().get(Integer.parseInt(chosenOption));
			choices.clear();

			getItemsToShow(actualMeetingRoom);
		} else {
			System.out.println("!!!!!!!!!!!!");
			System.out.println("There are no rooms in this buiding");
			System.out.println("!!!!!!!!!!!!");
		}
	}

	private void editReservation(MeetingRoom room) {
		// TODO list reservation as choices, after chosen reservation edit all
		if (!room.getReservations().isEmpty()) {
			Reservation reservation = getReservation(room);
			while (true) {
				String string = Choices.getInput("Enter Time FROM in format: HH:MM : " );
				Pattern pattern = Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
				Matcher matcher = pattern.matcher(string);
				if (string.equals("")) {
					break;
				}
				if (matcher.matches()) {
					reservation.setTimeFrom(string);
					break;
				} else {
					System.out.println("The Date in not valid. Try it again");
				}
			}
			while (true) {
				String string = Choices.getInput("Enter Time TO in format: HH:MM : " );
				Pattern pattern = Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
				Matcher matcher = pattern.matcher(string);
				if (string.equals("")) {
					break;
				}
				if (matcher.matches()) {
					reservation.setTimeTo(string);
					break;
				} else {
					System.out.println("The Date in not valid. Try it again");
				}
			}
			while (true) {
				String input = Choices.getInput("Enter Expected persons count: " );
				if (input.equals("")) {
					break;
				}
				int intHour = Integer.parseInt(input);
				if (intHour >= 1 && intHour <= actualMeetingRoom.getCapacity()) {
					reservation.setExpectedPersonCount(intHour);
					break;
				} else {
					System.out.println("Your si higher then Room Capacity. Try it again");
					System.out.println("The room capacity is " + actualMeetingRoom.getCapacity());
				}
			}
			while (true) {

				String string = Choices.getInput("Customer: " );
				Pattern pattern = Pattern.compile("^.{2,100}$");
				Matcher matcher = pattern.matcher(string);
				if (string.equals("")) {
					break;
				}
				if (matcher.matches()) {
					reservation.setCustomer(string);
					break;
				} else {
					System.out.println("The Costumer in not valid. Try it again");
				}
			}
			while (true) {
				if (actualMeetingRoom.isHasVideoConference()) {
					String videoConference = Choices.getInput("Do you need video conference yes/no: " );
					Pattern pattern = Pattern.compile("^(yes|no)$");
					Matcher matcher = pattern.matcher(videoConference);
					if (videoConference.equals("")) {
						break;
					}
					if (matcher.matches()) {
						boolean video = videoConference.equalsIgnoreCase("yes");
						reservation.setNeedVideoConference(video);
						break;
					} else {
						System.out.println("The VideoConference in not valid. Try it again");
					}
				} else {
					break;
				}
			}
			while (true) {

				String string = Choices.getInput("Note: " );
				Pattern pattern = Pattern.compile("^.{0,300}$");
				Matcher matcher = pattern.matcher(string);
				if (string.equals("")) {
					break;
				}
				if (matcher.matches()) {
					reservation.setNote(string);
					break;
				} else {
					System.out.println("The Note in not valid. Try it again");
				}
			}
			System.out.println("Your reservation was edited");
		} else {
			System.out.println("There are no reservations");
		}
	}

	private void addNewReservation(MeetingRoom actualMeetingRoom) {
		// TODO enter data one by one, add new reservation object to the actual
		// room, than inform about successful adding
		Reservation reservation = new Reservation();

		reservation.setMeetingRoom(actualMeetingRoom);
		while (true) {
			String string = Choices.getInput("Enter Date in format: DD.MM.YYYY : " );
			Pattern pattern = Pattern.compile("^([1-9]|[12]\\d|3[01]){1}.(0?[1-9]|1[012]){1}.\\d{4}$");
			Matcher matcher = pattern.matcher(string);
			if (matcher.matches()) {
				DateFormat format = new SimpleDateFormat("DD.MM.YYYY");
				Date date = null;
				try {
					date = format.parse(string);
				} catch (ParseException e) {
					e.printStackTrace();
				}
				reservation.setDate(date);
				break;
			} else {
				System.out.println("The Date in not valid. Try it again");
			}
		}
		while (true) {
			String string = Choices.getInput("Enter Time FROM in format: HH:MM : " );
			Pattern pattern = Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
			Matcher matcher = pattern.matcher(string);
			if (matcher.matches()) {

				reservation.setTimeFrom(string);
				break;
			} else {
				System.out.println("The Date in not valid. Try it again");
			}
		}
		while (true) {
			String string = Choices.getInput("Enter Time TO in format: HH:MM : " );
			Pattern pattern = Pattern.compile("^([0-9]|0[0-9]|1[0-9]|2[0-3]):[0-5][0-9]$");
			Matcher matcher = pattern.matcher(string);
			if (matcher.matches()) {

				reservation.setTimeTo(string);
				break;
			} else {
				System.out.println("The Date in not valid. Try it again");
			}
		}
		while (true) {
			//TODO: zvalidovat prázný vstup
			String input = Choices.getInput("Enter Expected persons count: ");
			int intHour = Integer.parseInt(input);
			if (intHour >= 1 && intHour <= actualMeetingRoom.getCapacity()) {
				reservation.setExpectedPersonCount(intHour);
				break;
			} else {
				System.out.println("Your si higher then Room Capacity. Try it again");
				System.out.println("The room capacity is " + actualMeetingRoom.getCapacity());
			}
		}
		while (true) {

			String string = Choices.getInput("Customer: " );
			Pattern pattern = Pattern.compile("^.{2,100}$");
			Matcher matcher = pattern.matcher(string);
			if (matcher.matches()) {

				reservation.setCustomer(string);
				break;
			} else {
				System.out.println("The Costumer in not valid. Try it again");
			}
		}
		while (true) {
			if (actualMeetingRoom.isHasVideoConference()) {
				String videoConference = Choices.getInput("Do you need video conference yes/no: " );
				Pattern pattern = Pattern.compile("^(yes|no)$");
				Matcher matcher = pattern.matcher(videoConference);
				if (matcher.matches()) {
					boolean video = videoConference.equalsIgnoreCase("yes");
					reservation.setNeedVideoConference(video);
					break;
				} else {
					System.out.println("The VideoConference in not valid. Try it again");
				}
			} else {
				break;
			}
		}
		while (true) {

			String string = Choices.getInput("Note: " );
			Pattern pattern = Pattern.compile("^.{0,300}$");
			Matcher matcher = pattern.matcher(string);
			if (matcher.matches()) {

				reservation.setNote(string);
				break;
			} else {
				System.out.println("The Note in not valid. Try it again");
			}
		}

		actualMeetingRoom.addReservation(reservation);
		System.out.println("The reservation was successful created");
	}

	private void deleteReservation(MeetingRoom room) {
		// TODO list all reservations as choices and let enter item for
		// deletion, delete it and inform about successful deletion
		if (!room.getReservations().isEmpty()) {
			Reservation reservation = getReservation(room);
			room.removeReservation(reservation);
		} else {
			System.out.println("There are no reservations");
		}
	}

	private void changeDate(MeetingRoom room) {
		// TODO let them enter new date in format YYYY-MM-DD, change the actual
		// date, list actual reservations on this date and menu by
		// getItemsToShow()
		if (!room.getReservations().isEmpty()) {
			Reservation reservation = getReservation(room);
			while (true) {
				String string = Choices.getInput("Enter Date in format: DD.MM.YYYY : ");
				Pattern pattern = Pattern.compile("^([1-9]|[12]\\d|3[01]){1}.(0?[1-9]|1[012]){1}.\\d{4}$");
				Matcher matcher = pattern.matcher(string);
				if (string.equals("")) {
					break;
				}
				if (matcher.matches()) {
					DateFormat format = new SimpleDateFormat("DD.MM.YYYY");
					Date date = null;
					try {
						date = format.parse(string);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					reservation.setDate(date);
					break;
				} else {
					System.out.println("The Date in not valid. Try it again");
				}
			}
		} else {
			System.out.println("There are no reservations");
		}
	}

	public Reservation getReservation(MeetingRoom room) {
		System.out.println("Ids of reservations ");
		String reservationId;
		int counter = 0;
		for (Reservation reservation : room.getReservations()) {
			System.out.println(counter + ": " + reservation.getDate() + " " + reservation.getTimeFrom() + " " + reservation.getTimeTo()+ " " +
					reservation.getExpectedPersonCount() + " " + reservation.getCustomer());
			counter++;
		}
		System.out.println();
		while (true) {
			reservationId = Choices.getInput("Enter Number of the reservation: ");
			if (!reservationId.equals("") && Integer.parseInt(reservationId) < room.getReservations().size()) {
				break;
			} else {
				System.out.println("Wrong number. Try it again");
				System.out.println();
			}
		}
		return room.getReservations().get(Integer.parseInt(reservationId));
	}
	//TODO: Dodělat aby si při zadání ničeho vypsalo menu znova
	private void getItemsToShow(MeetingRoom room) {
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
					editReservation(room);
					break;
				case 2:
					addNewReservation(room);
					break;
				case 3:
					deleteReservation(room);
					break;
				case 4:
					changeDate(room);
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
