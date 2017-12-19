package cz.unicorncollege.bt.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.json.JsonObject;

import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.controller.MeetingController;
import cz.unicorncollege.controller.ReservationController;

public class FileParser {

	/**
	 * Method to import data from the chosen file.
	 */
	public static List<MeetingCentre> importData() {

		String locationFilter = Choices.getInput("Enter path of imported file: ");

		List<MeetingCentre> allMeetingCentres = new ArrayList<>();

		// TODO: Nacist data z importovaneho souboru, pokud jste nesplnili v
		// prvnim ukolu, muzete zde naplnit data manualne
		System.out.println();

		System.out.println("**************************************************");
		System.out.println("Data was imported. " + allMeetingCentres.size() + " objects of MeetingCentres was loaded");
		System.out.println("**************************************************");

		System.out.println();

		return null;
	}

	/**
	 * Method to save the data to file.
	 */
	public static void saveData(MeetingController controll) {
		// TODO: ulozeni dat do XML souboru, jmeno souboru muze byt natvrdo,
		// adresar stejny jako se nachazi aplikace
		File fileToSaveXML = null;

		System.out.println();

		System.out.println("**************************************************");
		System.out.println("Data was saved correctly.");
		System.out.println("**************************************************");

		System.out.println();
	}

	/**
	 * Method to export data to JSON file
	 * 
	 * @param controllReservation
	 *            Object of reservation controller to get all reservation and
	 *            other data if needed
	 */
	public static void exportDataToJSON(ReservationController controllReservation) {
		// TODO: ulozeni dat do souboru ve formatu JSON

		String locationFilter = Choices.getInput("Enter name of the file for export: ");

		File exportDataFile = null;
		JsonObject json = null;

		System.out.println();

		if (exportDataFile != null) {
			System.out.println("**************************************************");
			System.out.println("Data was exported correctly. The file is here: " + exportDataFile.getAbsolutePath());
			System.out.println("**************************************************");
		} else {
			System.out.println("**************************************************");
			System.out.println("Something terrible happend during exporting!");
			System.out.println("**************************************************");
		}

		System.out.println();
	}

	/**
	 * Method to load the data from file.
	 * 
	 * @return
	 */
	public static List<MeetingCentre> loadDataFromFile() {
		// TODO: nacist data z XML souboru

		System.out.println();

		System.out.println("**************************************************");
		System.out.println("Data was loaded correctly.");
		System.out.println("**************************************************");

		System.out.println();

		return null;
	}
}
