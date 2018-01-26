package cz.unicorncollege.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.opencsv.CSVWriter;
import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.FileParser;

/**
 * Meeting Controller class.
 * Contains methods to use CRUD operations on meeting centres and meeting rooms
 *
 * @author Tadeas Firich
 */
public class MeetingController {
	private List<MeetingCentre> meetingCentres;

	/**
	 * Method to initialize data from the saved datafile.
	 */
	public void init() {

		meetingCentres = FileParser.loadDataFromFile();
	}

	/**
	 * Method to list all meeting centres to user and give him some options what to do next.
	 */
	public void listAllMeetingCentres() {
		System.out.println();
		System.out.println("Details of meeting centres:");
		for (int i = 0; i < getMeetingCentres().size(); i++) {
			MeetingCentre center = getMeetingCentres().get(i);
			System.out.print(center.getName() + ", ");
			System.out.print(center.getCode() + ", ");
			System.out.print(center.getDescription());
			System.out.println();
		}

		List<String> choices = new ArrayList<String>();
		choices.add("Show Details of Meeting Centre with code:");
		choices.add("Edit Meeting Centre with code:");
		choices.add("Delete Meeting Centre with code:");
		choices.add("Go Back to Home");

		System.out.println();
		System.out.println("-------------------------------------");

		String chosenOption;
		int option;
		String code;

		while (true) {
			try
			{
				chosenOption = Choices.showMenu("Choose option (including code after '-', example 1-M01): ", choices);
				option = chosenOption.contains("-") ? Integer.parseInt(chosenOption.substring(0, 1)) : Integer.parseInt(chosenOption);
				code = chosenOption.contains("-") ? chosenOption.substring(2, chosenOption.length()) : "";
				if (isMeetingCentreExist(code) || chosenOption.equals("4")) {
					break;
				} else {
					System.out.println("This option or Meeting Centre is not exist. Try it again");
				}
			} catch (NumberFormatException ex)
			{
				System.out.println("Wrong input. Try it again");
			}
		}

		while (true) {
			switch (option) {
				case 1:
					showMeetingCentreDetails(code);
					break;
				case 2:
					editMeetingCentre(code);
					return;
				case 3:
					deleteMeetingCentre(code);
					return;
				default: return;
			}
		}

	}

	/**
	 * Method to add a new meeting centre.
	 */

	public void addMeeMeetingCentre() {
		MeetingCentre newCenter = new MeetingCentre();
		while (true) {
			String code = (Choices.getInput("Enter Code of New MeetingCentre: "));
			Pattern pattern = Pattern.compile("[[a-zA-Z][.:_]]{5,50}");
			Matcher matcher = pattern.matcher(code);
			if (matcher.matches()) {
				if (isMeetingCentreExist(code)) {
					System.out.println("This Meeting Centre si already exist. Try it again");
				} else {
					newCenter.setCode(code);
					break;
				}
			} else {
				System.out.println("The Code in not valid. Try it again");
			}
		}
		while (true) {
			String name = Choices.getInput("Enter name of the MeetingCentre: " );
			Pattern pattern = Pattern.compile(".{2,100}");
			Matcher matcher = pattern.matcher(name);
			if (matcher.matches()) {
				newCenter.setName(name);
				break;
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}
		while (true) {
			String description = Choices.getInput("Enter description of the MeetingCentre: " );
			Pattern pattern = Pattern.compile(".{10,300}");
			Matcher matcher = pattern.matcher(description);
			if (matcher.matches()) {
				newCenter.setDescription(description);
				break;
			} else {
				System.out.println("The Description in not valid. Try it again");
			}
		}

		meetingCentres.add(newCenter);
		System.out.println("Your Meeting Centre was successful add");
		listAllMeetingCentres();
	}

	/**
	 * Method to show meeting centre details by id.
	 */
	public void showMeetingCentreDetails(String input) {
		MeetingCentre centre = findMeetingCentreByCode(input);
		System.out.println("Name: " + centre.getName());
		System.out.println("Code: " + centre.getCode());
		System.out.println("Description: "+centre.getDescription());

		List<String> choices = new ArrayList<String>();
		choices.add("Show meeting rooms");
		choices.add("Add meeting room");
		choices.add("Edit details");
		choices.add("Delete room");
		choices.add("Go Back");
		//FIXME: Cyklí se při zmačkunutí 4

		while (true) {
			switch (Choices.getChoice("Select an option: ", choices)) {
				case 1:
					showMeetingRooms(centre);
					break;
				case 2:
					addMeetingRoom(centre);
					break;
				case 3:
					editRoomDetials(centre);
					break;
				case 4:
					deleteRoom(centre);
					break;
				case 5:
					listAllMeetingCentres();
					break;
			}
		}
	}
	/**
	 * Show meeting rooms in the meeting centre
	 */
	public void showMeetingRooms(MeetingCentre centre) {
		System.out.println("Rooms in: " + centre.getName() + ": ");
		for (MeetingRoom room : centre.getMeetingRooms()) {
			System.out.println(room.getName() + ", " + room.getCode() + ", " + room.getDescription());
		}
	}
	/**
	 * Add meeting room to the meeting centre
	 */
	public void addMeetingRoom(MeetingCentre centre) {
		MeetingRoom theRoom = new MeetingRoom();

		while (true) {
			String code = Choices.getInput("Enter Code of the Meeting room: ");
			Pattern pattern = Pattern.compile("[[a-zA-Z][.:_]]{5,50}");
			Matcher matcher = pattern.matcher(code);

			if (matcher.matches()) {

				if ( isMeetingCentreExist(code)) {
					System.out.println("The Meeting room with this code is already exist");
				} else  {
					theRoom.setCode(code);
					break;
				}
			} else {
				System.out.println("The Code in not valid. Try it again");
			}
		}
		while (true) {
			String name = Choices.getInput("Enter name of the Meeting room: " );
			Pattern pattern = Pattern.compile(".{2,100}");
			Matcher matcher = pattern.matcher(name);
			if (matcher.matches()) {
				theRoom.setName(name);
				break;
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}
		while (true) {
			String description = Choices.getInput("Enter description of the Meeting room: " );
			Pattern pattern = Pattern.compile(".{10,300}");
			Matcher matcher = pattern.matcher(description);
			if (matcher.matches()) {
				theRoom.setDescription(description);
				break;
			} else {
				System.out.println("The Description in not valid. Try it again");
			}
		}
		while (true) {
			String capacity = Choices.getInput("Enter capacity of the Meeting room: " );
			int intCapacity = Integer.parseInt(capacity);
			if (intCapacity >= 1 && intCapacity <= 100) {
				theRoom.setCapacity(intCapacity);
				break;
			} else {
				System.out.println("The Capacity in not valid. Try it again");
			}
		}
		while (true) {
			String videoConference = Choices.getInput("Enter videoConference of the Meeting room yes/no: " );
			Pattern pattern = Pattern.compile("(yes|no)");
			Matcher matcher = pattern.matcher(videoConference);
			if (matcher.matches()) {
				boolean video = videoConference.equalsIgnoreCase("yes");
				theRoom.setHasVideoConference(video);
				break;
			} else {
				System.out.println("The VideoConference in not valid. Try it again");
			}
		}

		centre.addMeetingRoom(theRoom);
		System.out.println("The room was successful created");
	}
	/**
	 * Get code from meeting centre
	 *
	 * @return String code of the meeting centre
	 */
	public String getRoomCode(MeetingCentre centre) {
		System.out.println("Code of MeetingRooms: ");
		String meetingRoomCode;
		for (MeetingRoom room : centre.getMeetingRooms()) {
			System.out.println(room.getCode());
		}
		System.out.println();
		while (true) {
			meetingRoomCode = Choices.getInput("Enter Code of the Meeting room: ");
			if (isRoomExist(centre, meetingRoomCode)) {
				break;
			} else {
				System.out.println("Wrong code. Try it again");
				System.out.println();
			}
		}
		return meetingRoomCode;
	}
	/**
	 * Edit meeting room
	 */
	public void editRoomDetials(MeetingCentre centre) {
		String meetingRoomCode = getRoomCode(centre);
		MeetingRoom theRoom = findRoomByCode(centre, meetingRoomCode);
		System.out.println("Now you can edit your Meeting room");
		System.out.println();
		while (true) {
			String code = Choices.getInput("Enter Code of the Meeting room ("+ theRoom.getCode() + "): ");
			Pattern pattern = Pattern.compile("[[a-zA-Z][.:_]]{5,50}");
			Matcher matcher = pattern.matcher(code);
			if (code.equals("")) {
				break;
			}

			if (matcher.matches()) {

				if ( isMeetingCentreExist(code) && code != meetingRoomCode) {
					System.out.println("The Meeting room with this code is already exist");
					continue;
				} else if (!isMeetingCentreExist(code) && code != meetingRoomCode) {
					theRoom.setCode(code);
					break;
				}
				else if (code.equals(meetingRoomCode)) {
					break;
				}
			} else {
				System.out.println("The Code in not valid. Try it again");
			}
		}
		while (true) {
			String name = Choices.getInput("Enter name of the Meeting room (" + theRoom.getName() + "): " );
			Pattern pattern = Pattern.compile(".{2,100}");
			Matcher matcher = pattern.matcher(name);
			if (name.equals("")) {
				break;
			}
			if (matcher.matches()) {
				theRoom.setName(name);
				break;
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}
		while (true) {
			String description = Choices.getInput("Enter description of the Meeting room (" + theRoom.getDescription() + "): " );
			Pattern pattern = Pattern.compile(".{10,300}");
			Matcher matcher = pattern.matcher(description);
			if (description.equals("")) {
				break;
			}
			if (matcher.matches()) {
				theRoom.setDescription(description);
				break;
			} else {
				System.out.println("The Description in not valid. Try it again");
			}
		}
		while (true) {
			String capacity = Choices.getInput("Enter capacity of the Meeting room (" + theRoom.getCapacity() + "): " );
			if (capacity.equals("")) {
				break;
			}
			int intCapacity = Integer.parseInt(capacity);
			if (intCapacity >= 1 && intCapacity <= 100) {
				theRoom.setCapacity(intCapacity);
				break;
			} else {
				System.out.println("The Capacity in not valid. Try it again");
			}
		}
		while (true) {
			String videoConference = Choices.getInput("Enter videoConference of the Meeting room (" + theRoom.isHasVideoConference() + ") yes/no: " );
			Pattern pattern = Pattern.compile("(yes|no)");
			Matcher matcher = pattern.matcher(videoConference);
			if (videoConference.equals("")) {
				break;
			}
			if (matcher.matches()) {
				boolean video = videoConference.equalsIgnoreCase("yes");
				theRoom.setHasVideoConference(video);
				break;
			} else {
				System.out.println("The VideoConference in not valid. Try it again");
			}
		}
		System.out.println("The Meeting room was successful update: ");
		System.out.println(theRoom.getCode() +" "+ theRoom.getName() + " " + theRoom.getDescription() + " " + theRoom.getCapacity() + " " + theRoom.isHasVideoConference() + " " + theRoom.getMeetingCentre());
	}
	/**
	 * Delete meeting room
	 */
	public void deleteRoom(MeetingCentre centre) {
		String meetingRoomCode = getRoomCode(centre);
		while (true){
			System.out.println("Do you want to delete " + meetingRoomCode + " room?");
			String answer = Choices.getInput("yes/no: " );
			Pattern pattern = Pattern.compile("(yes|no)");
			Matcher matcher = pattern.matcher(answer);
			if (matcher.matches()) {
				boolean boolAnswer = answer.equalsIgnoreCase("yes");
				if (boolAnswer) {
					centre.removeMeetingRoom(findRoomByCode(centre, meetingRoomCode));
					System.out.println("The room was delete");
					break;
				} else {
					break;
				}
			} else {
				System.out.println("The Answer in not valid. Try it again");
			}
		}
	}
	/**
	 * Is this room exist?
	 *
	 * @return boolean
	 */
	public boolean isRoomExist(MeetingCentre centre, String code) {
		boolean isFounded = false;

		for (MeetingRoom room : centre.getMeetingRooms()) {
			if (code.equals(room.getCode())) {
				isFounded = true;
			}
		}

		return isFounded;
	}
	/**
	 * Find meeting room in the meeting centre by code
	 *
	 * @return MeetingRoom object
	 */
	public MeetingRoom findRoomByCode(MeetingCentre centre, String code) {
		MeetingRoom foundRoom = null;
		boolean isFounded = false;
		for (MeetingRoom room : centre.getMeetingRooms()) {
			if (code.equals(room.getCode())) {
				foundRoom = room;
				isFounded = true;
			}
		}
		if (!isFounded) {
			System.out.println("Room " + code + " was not find");
			foundRoom = null;
		}
		return foundRoom;
	}

	/**
	 * Method to edit meeting centre data by id.
	 */
	public void editMeetingCentre(String input) {
		MeetingCentre centre = findMeetingCentreByCode(input);
		String codeBefore = centre.getCode();
		String name;
		String code;
		String description;

		while (true) {
			code = Choices.getInput("Enter Code of New MeetingCentre ("+ centre.getCode() + "): ");
			Pattern pattern = Pattern.compile("[[a-zA-Z][.:_]]{5,50}");
			Matcher matcher = pattern.matcher(code);
			if (code.equals("")) {
				break;
			}

			if (matcher.matches()) {

				if ( isMeetingCentreExist(code) && code != codeBefore) {
					System.out.println("The Meeting Centre with this code is already exist");
					continue;
				} else if (!isMeetingCentreExist(code) && code != codeBefore) {
					centre.setCode(code);
					break;
				}
				else if (code.equals(codeBefore)) {
					break;
				}
			} else {
				System.out.println("The Code in not valid. Try it again");
			}
		}
		while (true) {
			name = Choices.getInput("Enter name of the MeetingCentre (" + centre.getName() + "): " );
			Pattern pattern = Pattern.compile(".{2,100}");
			Matcher matcher = pattern.matcher(name);
			if (name.equals("")) {
				break;
			}
			if (matcher.matches()) {
				centre.setName(name);
				break;
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}
		while (true) {
			description = Choices.getInput("Enter description of the MeetingCentre (" + centre.getDescription() + "): " );
			Pattern pattern = Pattern.compile(".{10,300}");
			Matcher matcher = pattern.matcher(description);
			if (description.equals("")) {
				break;
			}
			if (matcher.matches()) {
				centre.setDescription(description);
				break;
			} else {
				System.out.println("The Description in not valid. Try it again");
			}
		}
		System.out.println("The Meeting Centre was successful update: ");
		System.out.println(centre.getCode() +" "+ centre.getName() + " " + centre.getDescription());
	}
	/**
	 * Is this Meeting Centre exist?
	 *
	 * @return boolean
	 */
	public boolean isMeetingCentreExist(String code) {
		boolean isFounded = false;
		for (MeetingCentre centre : meetingCentres) {
			if (code.equals(centre.getCode())) {
				isFounded = true;
			}
		}
		return isFounded;
	}

	/**
	 * Method to delete by id
	 */
	public void deleteMeetingCentre(String input) {
		MeetingCentre centre = findMeetingCentreByCode(input);
		if (centre != null) {
			String stringRemove = Choices.getInput("Do you want to delete: " + centre.getCode() + " TRUE/FALSE: ");
			boolean remove = stringRemove.equalsIgnoreCase("true") || stringRemove.equalsIgnoreCase("TRUE");
			if (remove) {
				meetingCentres.remove(centre);
				System.out.println("Centrum" + input + " was delete");
			} else {
				System.out.println("Mazaní neproběhlo");
			}
		}

		listAllMeetingCentres();
	}
	/**
	 * Find meeting centre by the code
	 *
	 * @return MeetingCentre object
	 */
	public MeetingCentre findMeetingCentreByCode(String code) {
		MeetingCentre foundCentre = null;
		boolean isFounded = false;
		for (MeetingCentre centre : meetingCentres) {
			if (code.equals(centre.getCode())) {
				foundCentre = centre;
				isFounded = true;
			}
		}
		if (!isFounded) {
			System.out.println("Centrum " + code + " nebylo nalezeno");
			foundCentre = null;
		}
		return foundCentre;
	}

	/**
	 * Method to get all data to save in string format
	 * @return
	 */
	public String toSaveString() {
		String headerCentres = "MEETING_CENTRES,,,,,\r\n";
		String headerRooms = "MEETING_ROOMS,,,,,\r\n";

		String meetingCenters = "";
		String meetingRooms = "";
		for (MeetingCentre center : this.meetingCentres) {
			meetingCenters += center.getName() + CSVWriter.DEFAULT_SEPARATOR + center.getCode() + CSVWriter.DEFAULT_SEPARATOR + center.getDescription() + "\r\n";
			center.getMeetingRooms();
			for (MeetingRoom room : center.getMeetingRooms()) {
				String videoConference;
				if (room.isHasVideoConference()) {
					videoConference = "YES";
				} else {
					videoConference = "NO";
				}

				meetingRooms += room.getName() + CSVWriter.DEFAULT_SEPARATOR + room.getCode() + CSVWriter.DEFAULT_SEPARATOR + room.getDescription() + CSVWriter.DEFAULT_SEPARATOR + room.getCapacity()
						+ CSVWriter.DEFAULT_SEPARATOR + videoConference + CSVWriter.DEFAULT_SEPARATOR + room.getMeetingCentre().getCode() + "\r\n";
			}
		}
		return headerCentres + meetingCenters + headerRooms + meetingRooms;
	}
	/**
	 * Get all meeting centres
	 *
	 * @return List<MeetingCentre> all meeting centres
	 */
	public List<MeetingCentre> getMeetingCentres() {
		return meetingCentres;
	}
	/**
	 * Set meeting centres
	 */
	public void setMeetingCentres(List<MeetingCentre> meetingCentres) {
		this.meetingCentres = meetingCentres;
	}
}
