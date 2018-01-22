package cz.unicorncollege.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.unicorncollege.bt.model.Addon;
import cz.unicorncollege.bt.model.Category;
import cz.unicorncollege.bt.utils.Choices;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.service.ServiceRegistry;

public class AddonsController {

	private static final SessionFactory sessionFactory = buildSessionFactory();

	private static SessionFactory buildSessionFactory() {
		// Create a StandardServiceRegistry
		final ServiceRegistry registry = new StandardServiceRegistryBuilder().configure().build();
		return new MetadataSources(registry).buildMetadata().buildSessionFactory();
	}

	public void showAddonsMenu() {
		List<String> choices = new ArrayList<String>();
		choices.add("List all Addons");
		choices.add("Add new Addon");
		choices.add("Edit Addon");
		choices.add("Delete Addon");
		choices.add("Accept delivery");
		choices.add("Hand Over");

		while (true) {
			switch (Choices.getChoice("Choose what do you want to do: ", choices)) {
			case 1:
				listAllAddons();
				break;
			case 2:
				addNewAddon();
				break;
			case 3:
				editAddon();
				break;
			case 4:
				deleteAddon();
				break;
			case 5:
				acceptDelivery();
				break;
			case 6:
				handOver();
				break;
			case 7:
				return;
			}
		}
		
	}
	/**
	 * Show list of addons
	 */
	
	private void listAllAddons() {
		Session session = sessionFactory.openSession();

		// Create Criteria
		Criteria criteria = session.createCriteria(Addon.class);

		// Get a list of Contact objects according to the Criteria object
		List<Addon> addons = criteria.list();

		// Close the session
		session.close();

		System.out.println("Id, Name, Category, Amount");
		for (Addon addon : addons) {
			System.out.println(addon.getId() + ", " + addon.getName() + ", " + addon.getCategory().getName() + ", " + addon.getAmount());
		}
	}

	/**
	 * Add new addon in database
	 */
	
	private void addNewAddon() {
		Addon addon = new Addon();

		addon.setCategory(findCategoryByCode());

		while (true) {

			String string = Choices.getInput("Addon's name: " );
			if (string != null) {

				addon.setName(string);
				break;
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}
		while (true) {

			String string = Choices.getInput("Amount of items (max:" + addon.getMinimalAmount() + "): ");
			if (string != null) {
				try
				{
					Integer.parseInt(string);
				} catch (NumberFormatException ex)
				{
					System.out.println("This is not a number. Try it again");
					continue;
				}
				if (Integer.parseInt(string) <= addon.getMinimalAmount()) {
					addon.setAmount(Integer.parseInt(string));
					break;
				} else {
					System.out.println("Your number is too large. Try it again");
				}
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}

		// Open a session
		Session session = sessionFactory.openSession();

		// Begin a transaction
		session.beginTransaction();

		// Use the session to save the contact
		session.save(addon);

		// Commit the transaction
		session.getTransaction().commit();

		// Close the session
		session.close();
		System.out.println("Addon was successfully added");
	}

	private void editAddon() {
		listAllAddons();



		Addon addon = addonMenu();

		Session session = sessionFactory.openSession();

		// Begin a transaction
		session.beginTransaction();

		// Use the session to update the contact
		session.update(addon);

		// Commit the transaction
		session.getTransaction().commit();

		// Close the session
		session.close();
	}

	private void deleteAddon() {
		listAllAddons();
	}
	
	private void acceptDelivery() {
		
	}
	
	private void handOver() {

	}

	private static Addon addonMenu() {
		Addon addon;

		while (true) {
			long id;
			boolean isFounded = false;
			String string = Choices.getInput("Enter Id for edit Addon: ");
			try
			{
				id = Long.valueOf(string);
			} catch (NumberFormatException ex)
			{
				System.out.println("This is not a number. Try it again");
				continue;
			}

			addon = findAddonById(id);
			if (addon != null) {
				break;
			}
		}
		return addon;
	}

	private static Addon findAddonById(long id) {
		Session session = sessionFactory.openSession();

		// Retrieve the persistent object (or null if not found)
		Addon addon = session.get(Addon.class, id);

		// Close the session
		session.close();

		// Return the object
		return addon;
	}

	public Category findCategoryByCode() {
		Session session = sessionFactory.openSession();
		// Create Criteria
		Criteria criteria = session.createCriteria(Category.class);

		// Get a list of Contact objects according to the Criteria object
		List<Category> categories = criteria.list();

		// Close the session
		session.close();
		System.out.println("------------------");
		System.out.println("List of Categories");
		System.out.println("------------------");
		System.out.println("Code, Name");
		for (Category category : categories) {
			System.out.println(category.getCode() + ", " + category.getName());
		}

		Category foundCategory = null;
		while (true) {
			boolean isFounded = false;
			String categoryCode = Choices.getInput("Enter Code of the Category: ");
			for (Category category : categories) {
				if (categoryCode.equals(category.getCode())) {
					foundCategory = category;
					isFounded = true;
				}
			}
			if (!isFounded) {
				System.out.println("There is no category: " + categoryCode);
				foundCategory = null;
			} else {
				break;
			}
		}
		return foundCategory;
	}
}
