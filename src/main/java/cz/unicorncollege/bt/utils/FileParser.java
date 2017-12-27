package cz.unicorncollege.bt.utils;

import com.opencsv.CSVReader;
import cz.unicorncollege.bt.model.MeetingCentre;
import cz.unicorncollege.bt.model.MeetingRoom;
import cz.unicorncollege.bt.model.Reservation;
import org.w3c.dom.*;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import static java.lang.String.*;

public class FileParser {

	/**
	 * Method to import data from the chosen file.
	 */
	public static List<MeetingCentre> importData() {

		String locationFilter = Choices.getInput("Enter path of imported file: ");

		List<MeetingCentre> allMeetingCentres = new ArrayList<>();
		allMeetingCentres = readData(locationFilter);

		System.out.println();

		System.out.println("**************************************************");
		System.out.println("Data was imported. " + allMeetingCentres.size() + " objects of MeetingCentres was loaded");
		System.out.println("**************************************************");

		System.out.println();

		return allMeetingCentres;
	}

	public static List<MeetingCentre> readData(String locationFilter) {
		List<MeetingCentre> allMeetingCentres = new ArrayList<>();
		CSVReader reader = null;

		File myFile = new File(locationFilter);
		if (myFile.exists() && !myFile.isDirectory()) {
			try {
				reader = new CSVReader(new FileReader(locationFilter));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
			String[] nextLine;
			try {
				int counter;
				counter = 0;
				while ((nextLine = reader.readNext()) != null) {
					MeetingCentre center = new MeetingCentre();
					if (counter == 0) {
						counter = 1;
					} else if (nextLine[1].equals("")) {
						counter++;
					} else if (counter == 1) {
						center.setName(nextLine[0]);
						center.setCode(nextLine[1]);
						center.setDescription(nextLine[2]);
						allMeetingCentres.add(center);
					} else {
						MeetingRoom room = new MeetingRoom();
						room.setName(nextLine[0]);
						room.setCode(nextLine[1]);
						room.setDescription(nextLine[2]);
						room.setCapacity(Integer.parseInt(nextLine[3]));
						if (Objects.equals(nextLine[4], "YES")) {
							room.setHasVideoConference(true);
						} else {
							room.setHasVideoConference(false);
						}
						for (int i = 0; i < allMeetingCentres.size(); i++) {
							if (nextLine[5].equals(allMeetingCentres.get(i).getCode())) {
								room.setMeetingCentre(allMeetingCentres.get(i));
							}
						}
						for (int i = 0; i < allMeetingCentres.size(); i++) {
							MeetingCentre theCenter = room.getMeetingCentre();
							if (theCenter.equals(allMeetingCentres.get(i))) {
								theCenter.addMeetingRoom(room);
							}
						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("There is no import File");
		}
		return allMeetingCentres;
	}

	/**
	 * Method to save the data to file.
	 */
	public static void saveData(String output) {
		File file = new File("myFile.csv");

		try {
			file.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}

		FileWriter writer = null;
		try {
			writer = new FileWriter(file);
		} catch (IOException e) {
			e.printStackTrace();
		}

		try {
			writer.write(output);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println();

		System.out.println("**************************************************");
		System.out.println("Data was saved correctly.");
		System.out.println("**************************************************");

		System.out.println();
	}

	/**
	 * Method to load the data from file.
	 *
	 * @return
	 */
	public static List<MeetingCentre> loadDataFromFile() {
		List<MeetingCentre> myMeetingCentres = new ArrayList<>();
		String location = "myFile.csv";
		String locationXML = "reservations.xml";
		File csvFile = new File(location);
		File xmlFile = new File(location);
		if (csvFile.exists() && !csvFile.isDirectory()) {
			myMeetingCentres = readData(location);
			if (xmlFile.exists() && !xmlFile.isDirectory()) {
				loadReservationsFromXML(myMeetingCentres, locationXML);
                System.out.println();
                System.out.println("**************************************************");
                System.out.println("Reservations data was loaded correctly.");
                System.out.println("**************************************************");
			} else {
				System.out.println();
                System.out.println("**************************************************");
                System.out.println("The is no Reservation's Data");
                System.out.println("**************************************************");
			}
			System.out.println();

			System.out.println("**************************************************");
			System.out.println("Meeting centres data was loaded correctly.");
			System.out.println("**************************************************");

			System.out.println();

		} else {
			System.out.println("**************************************************");
			System.out.println("The is no Meeting centres Data");
			System.out.println("PLEASE IMPORT THE DATA.");
			System.out.println("**************************************************");
		}

		return myMeetingCentres;
	}

	public static void loadReservationsFromXML(List<MeetingCentre> meetingCentre, String locationXML) {
		try {

			File fXmlFile = new File(locationXML);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.parse(fXmlFile);

			NodeList nList = doc.getElementsByTagName("reservation");

			for (int temp = 0; temp < nList.getLength(); temp++) {

				Node nNode = nList.item(temp);

				System.out.println("\nCurrent Element :" + nNode.getNodeName());

				if (nNode.getNodeType() == Node.ELEMENT_NODE) {

					Element eElement = (Element) nNode;

					Reservation reservation = new Reservation();

					String room = (eElement.getElementsByTagName("room").item(0).getTextContent());

					//Předelat na DATE
					String string = eElement.getElementsByTagName("date").item(0).getTextContent();

					DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
					Date date = null;
					try {
						date = format.parse(string);
					} catch (ParseException e) {
						e.printStackTrace();
					}
					reservation.setDate(date);

					reservation.setTimeFrom(eElement.getElementsByTagName("timeFrom").item(0).getTextContent());
					reservation.setTimeTo(eElement.getElementsByTagName("timeTo").item(0).getTextContent());
					int count = Integer.parseInt(eElement.getElementsByTagName("expectedPersonCount").item(0).getTextContent());
					reservation.setExpectedPersonCount(count);

					reservation.setCustomer(eElement.getElementsByTagName("costumer").item(0).getTextContent());
					reservation.setNote(eElement.getElementsByTagName("note").item(0).getTextContent());

					for (MeetingCentre centre : meetingCentre) {
						for (MeetingRoom meetingRoom : centre.getMeetingRooms()) {
							if (meetingRoom.getCode().equals(room)) {
								reservation.setMeetingRoom(meetingRoom);
								meetingRoom.addReservation(reservation);
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static void saveDataToXML(List<MeetingCentre> centres) throws Exception {
		// TODO: ulozeni dat do souboru ve formatu JSON
		// TODO: Pořešit, když nejsou žádné rezervace

		try {
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
			Document doc = dBuilder.newDocument();

			// root element
			Element rootElement = doc.createElement("reservatons");
			doc.appendChild(rootElement);

			int hasNoReservations = 0;
			for (MeetingCentre centre : centres) {
				for (MeetingRoom room : centre.getMeetingRooms()) {
					if (room.getReservations().isEmpty()) {
						hasNoReservations++;
						//TODO: Dodelat vyjímku
					} else {
						for (Reservation meetingReservation : room.getReservations()) {
							Element reservation = doc.createElement("reservation");
							rootElement.appendChild(reservation);

							Element meetingRoom = doc.createElement("room");
							meetingRoom.appendChild(doc.createTextNode(meetingReservation.getMeetingRoom().getCode()));
							reservation.appendChild(meetingRoom);

							Element date = doc.createElement("date");
							date.appendChild(doc.createTextNode(meetingReservation.getFormattedDate()));
							reservation.appendChild(date);

							Element timeFrom = doc.createElement("timeFrom");
							timeFrom.appendChild(doc.createTextNode(meetingReservation.getTimeFrom()));
							reservation.appendChild(timeFrom);

							Element timeTo = doc.createElement("timeTo");
							timeTo.appendChild(doc.createTextNode(meetingReservation.getTimeTo()));
							reservation.appendChild(timeTo);

							Element count = doc.createElement("expectedPersonCount");
							count.appendChild(doc.createTextNode(Integer.toString(meetingReservation.getExpectedPersonCount())));
							reservation.appendChild(count);

							//Boolean.toString

							if (room.isHasVideoConference()) {
								Element conference = doc.createElement("hasVideoConference");
								conference.appendChild(doc.createTextNode(valueOf(meetingReservation.isNeedVideoConference())));
								reservation.appendChild(conference);
							}
							Element costumer = doc.createElement("costumer");
							costumer.appendChild(doc.createTextNode(meetingReservation.getCustomer()));
							reservation.appendChild(costumer);

							Element note = doc.createElement("note");
							note.appendChild(doc.createTextNode(meetingReservation.getNote()));
							reservation.appendChild(note);
						}
					}
				}
			}


			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer trasformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(rootElement);

			StreamResult streamResult = new StreamResult(new File("reservations.xml"));

			trasformer.transform(source, streamResult);


		} catch (Exception e) {
			e.printStackTrace();
		}


//		System.out.println();
//
//		if (exportDataFile != null) {
//			System.out.println("**************************************************");
//			System.out.println("Data was exported correctly. The file is here: " + exportDataFile.getAbsolutePath());
//			System.out.println("**************************************************");
//		} else {
//			System.out.println("**************************************************");
//			System.out.println("Something terrible happend during exporting!");
//			System.out.println("**************************************************");
//		}

		System.out.println();
	}

	public static void exportToJSON(List<MeetingCentre> centres) throws IOException {
		String location = Choices.getInput("Enter path for save JSON: ");

		JSONObject obj = new JSONObject();
		obj.put("schema", "PLUS4U.EBC.MCS.MeetingRoom_Schedule_1.0");
		obj.put("uri", "ues:UCL-BT:UCL.INF/DEMO_REZERVACE:EBC.MCS.DEMO/MR001/SCHEDULE");
        JSONArray data = new JSONArray();

        for (MeetingCentre centre : centres) {
            if (centre.hasCentreReservation()) {

				for (MeetingRoom room : centre.getMeetingRooms()) {
					if (!room.getReservations().isEmpty()) {
						JSONObject meetingCentre = new JSONObject();
						meetingCentre.put("meetingCentre", centre.getCode());
						meetingCentre.put("meetingRoom", room.getCode());
						JSONObject reservations = new JSONObject();

						Set<String> hashKeys = room.getReservationsByDate().keySet();
						for (String hashKey : hashKeys) {
							JSONArray date = new JSONArray();
							for (Reservation reservation : room.getReservationsByDate().get(hashKey)) {
								JSONObject singleReservation = new JSONObject();
								singleReservation.put("from", reservation.getTimeFrom());
								singleReservation.put("to", reservation.getTimeTo());
								singleReservation.put("expectedPersonsCount", reservation.getExpectedPersonCount());
								singleReservation.put("customer", reservation.getCustomer());
								singleReservation.put("videoConference", reservation.isNeedVideoConference());
								singleReservation.put("note", reservation.getNote());
								date.add(singleReservation);
							}
							reservations.put(hashKey, date);
						}
						meetingCentre.put("reservations", reservations);
						data.add(meetingCentre);
					}
				}
			}
        }

		obj.put("data", data);

		try (FileWriter file = new FileWriter(location)) {
			file.write(obj.toJSONString());
			System.out.println("Successfully Copied JSON Object to File..." + location);
			System.out.println("\nJSON Object: " + obj);
		}
	}
}