package cz.unicorncollege.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.FileParser;

/**
 * Main controller class.
 * Contains methods to communicate with user and methods to work with files.
 *
 * @author UCL
 */
public class MainController {
	private MeetingController controll;
	private ReservationController controllReservation;
	//TODO: Neošetřený prázdný vstup z menu. Chybí komentáře v kódu.

	/**
	 * Constructor of main class.
	 */
	public MainController() {
		controll = new MeetingController();
		controll.init();

		controllReservation = new ReservationController(controll);
	}


	/**
	 * Main method, which runs the whole application.
	 *
	 * @param argv String[]
	 */
	public static void main(String[] argv) {
		MainController instance = new MainController();
		instance.run();
	}

	/**
	 * Method which shows the main menu and end after user chooses Exit.
	 */
	private void run() {
		List<String> choices = new ArrayList<String>();
		choices.add("List all Meeting Centres");
		choices.add("Add new Meeting Centre");
		choices.add("Reservations");
		choices.add("Import Data");
		choices.add("Export Data");
		choices.add("Exit and Save");
		choices.add("Exit");
		System.out.println();
		System.out.println("-------------------------------------");
		while (true) {
			switch (Choices.getChoice("Select an option: ", choices)) {
				case 1:
					controll.listAllMeetingCentres();
					break;
				case 2:
					controll.addMeeMeetingCentre();
					break;
				case 3:
					controllReservation.showReservationMenu();
					break;
				case 4:
					controll.setMeetingCentres(FileParser.importData());
					controll.listAllMeetingCentres();
					break;
				case 5:
					try {
						FileParser.exportToJSON(controll.getMeetingCentres());
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case 6:
					FileParser.saveData(controll.toSaveString());
					try {
						FileParser.saveDataToXML(controll.getMeetingCentres());
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.exit(0);
				case 7:
					System.exit(0);
			}
		}
	}
}
