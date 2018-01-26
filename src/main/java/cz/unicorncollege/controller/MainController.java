package cz.unicorncollege.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cz.unicorncollege.bt.model.Addon;
import cz.unicorncollege.bt.model.Category;
import cz.unicorncollege.bt.utils.Choices;
import cz.unicorncollege.bt.utils.FileParser;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

import org.hibernate.cfg.Configuration;

/**
 * Main controller class.
 * Contains methods to communicate with user and methods to work with files.
 *
 * @author UCL
 */
public class MainController {
	private MeetingController controll;
	private ReservationController controllReservation;
	private AddonsController addOnsController;
	private static SessionFactory factory;
	private static Session session;

	/**
	 * Get instance of opened session
	 * @return
	 */
	public static Session getSession() {
		if (session != null && session.isOpen()) {
			return session;
		} else {
			session = factory.openSession();
			return session;
		}
	}

	/**
	 * Constructor of main class.
	 */
	public MainController() {
		controll = new MeetingController();
		controll.init();

		controllReservation = new ReservationController(controll);

		addOnsController = new AddonsController();
	}


	/**
	 * Main method, which runs the whole application.
	 *
	 * @param argv String[]
	 */
	public static void main(String[] argv) {
		factory = new Configuration().configure().buildSessionFactory();

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
		choices.add("Addons menu");
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
					addOnsController.showAddonsMenu();
					break;
				case 5:
					controll.setMeetingCentres(FileParser.importData());
					controll.listAllMeetingCentres();
					break;
				case 6:
					try {
						FileParser.exportToJSON(controll.getMeetingCentres());
					} catch (IOException e) {
						e.printStackTrace();
					}
					break;
				case 7:
					FileParser.saveData(controll.toSaveString());
					try {
						FileParser.saveDataToXML(controll.getMeetingCentres());
					} catch (Exception e) {
						e.printStackTrace();
					}
					System.exit(0);
				case 8:
					System.exit(0);
			}
		}
	}
}
