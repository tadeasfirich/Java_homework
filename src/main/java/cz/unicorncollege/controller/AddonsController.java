package cz.unicorncollege.controller;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.unicorncollege.bt.model.Addon;
import cz.unicorncollege.bt.model.AddonDelivery;
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
		choices.add("Helper");
		choices.add("Back to menu");

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
				helper();
				break;
			case 8:
				return;
			}
		}
		
	}

	private void helper() {
		Session session = sessionFactory.openSession();

		// Create Criteria
		Criteria criteria = session.createCriteria(AddonDelivery.class);

		// Get a list of Contact objects according to the Criteria object
		List<AddonDelivery> addons = criteria.list();

		// Close the session
		session.close();
		String mark = "";
		System.out.println("Id, Name, Category, MinAmount, Amount , MaxAmount");
		for (AddonDelivery addon : addons) {
			System.out.println(addon.getId() + ", " + addon.getCategory().getName() + ", " + addon.getAmount() + ", " + addon.getCustomerName() + ", " + addon.getDatetime());
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
		String mark = "";
		System.out.println("Id, Name, Category, MinAmount, Amount , MaxAmount");
		for (Addon addon : addons) {
			if (!addon.isDeleted()) {
				if (addon.getAmount() < addon.getMinimalAmount()) {
					mark = "!!";
				}
				System.out.println(mark + addon.getId() + ", " + addon.getName() + ", " + addon.getCategory().getName() + ", " + addon.getMinimalAmount() + ", " + addon.getAmount() + ", " + addon.getMaximalAmount() + mark);
				mark = "";
			}
		}
	}
	private void filtredListAllAddons(Category category) {
		Session session = sessionFactory.openSession();

		// Create Criteria
		Criteria criteria = session.createCriteria(Addon.class);

		// Get a list of Contact objects according to the Criteria object
		List<Addon> addons = criteria.list();

		// Close the session
		session.close();
		String mark = "";
		System.out.println("Id, Name, Category, MinAmount, Amount , MaxAmount");
		for (Addon addon : addons) {
			if (category != addon.getCategory()) {
				if (!addon.isDeleted()) {
					if (addon.getAmount() < addon.getMinimalAmount()) {
						mark = "!!";
					}
					System.out.println(mark + addon.getId() + ", " + addon.getName() + ", " + addon.getCategory().getName() + ", " + addon.getMinimalAmount() + ", " + addon.getAmount() + ", " + addon.getMaximalAmount() + mark);
					mark = "";
				}
			}
		}
	}
	/**
	 * Show and return list of addons
	 */
	private List<Addon> returnListAllAddons() {
		Session session = sessionFactory.openSession();

		// Create Criteria
		Criteria criteria = session.createCriteria(Addon.class);

		// Get a list of Contact objects according to the Criteria object
		List<Addon> addons = criteria.list();

		// Close the session
		session.close();

		System.out.println("Id, Name, Category, Amount, MaxAmount");
		for (Addon addon : addons) {
			System.out.println(addon.getId() + ", " + addon.getName() + ", " + addon.getCategory().getName() + ", " + addon.getAmount() + ", " + addon.getMaximalAmount());
		}
		return addons;
	}

	/**
	 * Add new addon in database
	 */
	
	private void addNewAddon() {
		Addon addon = new Addon();

		addon.setCategory(findCategoryByCode());

		while (true) {
			String string = Choices.getInput("Addon's name: " );
			Pattern pattern = Pattern.compile(".{2,100}");
			Matcher matcher = pattern.matcher(string);
			if (matcher.matches()) {
				addon.setName(string);
				break;
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}

		while (true) {

			String string = Choices.getInput("Recommended amount of items (Max:" + addon.getMaximalAmount() + "): ");
			if (string != null) {
				int amount;
				try
				{
					amount = Integer.parseInt(string);
				} catch (NumberFormatException ex)
				{
					System.out.println("This is not a number. Try it again");
					continue;
				}
				if (amount >= 0 && amount <= addon.getMaximalAmount()) {
					addon.setMinimalAmount(amount);
					break;
				} else {
					System.out.println("Your number is too large. Try it again");
				}
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}

		while (true) {

			String string = Choices.getInput("Amount of items (Max:" + addon.getMaximalAmount() + "): ");
			if (string != null) {
				int amount;
				try
				{
					amount = Integer.parseInt(string);
				} catch (NumberFormatException ex)
				{
					System.out.println("This is not a number. Try it again");
					continue;
				}
				if (amount >= 0 && amount <= addon.getMaximalAmount()) {
					addon.setAmount(amount);
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
	/**
	 * Edit addon to database
	 */
	private void editAddon() {
		listAllAddons();

		Addon addon = addonMenu();

		addon.setCategory(findCategoryByCode(addon.getCategory()));

		while (true) {
			String string = Choices.getInput("Addon's name" + addon.getName() + ": " );
			Pattern pattern = Pattern.compile(".{2,100}");
			Matcher matcher = pattern.matcher(string);
			if (string.equals("")) {
				break;
			}
			if (matcher.matches()) {
				addon.setName(string);
				break;
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}

		while (true) {

			String string = Choices.getInput("Minimal Recommended Amount of items (" + addon.getMinimalAmount() + "): ");
			if (string.equals("")) {
				break;
			}
			int amount;
			try
			{
				amount = Integer.parseInt(string);
			} catch (NumberFormatException ex)
			{
				System.out.println("This is not a number. Try it again");
				continue;
			}
			if (amount >= 0) {
				addon.setMinimalAmount(amount);
				break;
			} else {
				System.out.println("Your number is too small. Try it again");
			}
		}

		while (true) {

			String string = Choices.getInput("Maximal Amount of items (" + addon.getMaximalAmount() + "): ");
			if (string.equals("")) {
				break;
			}
			int amount;
			try
			{
				amount = Integer.parseInt(string);
			} catch (NumberFormatException ex)
			{
				System.out.println("This is not a number. Try it again");
				continue;
			}
			if (amount >= 0) {
				addon.setMaximalAmount(amount);
				break;
			} else {
				System.out.println("Your number is too small. Try it again");
			}
		}

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
	/**
	 * Makred addon as deleted
	 */
	private void deleteAddon() {
		List<Addon> addons = returnListAllAddons();
		Addon theAddon = new Addon();
		String id = Choices.getInput("Enter Id for delete the Addon: ");
		for (Addon addon : addons) {
			if (addon.getId() == Long.valueOf(id)) {
				theAddon = addon;
				break;
			}
		}
		theAddon.setDeleted(true);

		Session session = sessionFactory.openSession();

		// Begin a transaction
		session.beginTransaction();

		// Use the session to update the contact
		session.update(theAddon);

		// Commit the transaction
		session.getTransaction().commit();

		// Close the session
		session.close();
	}
	
	private void acceptDelivery() {
		Category category = findCategoryByCode();
		filtredListAllAddons(category);
		Addon addon = addonMenu();
		System.out.println("Name, Category, Min Amount, Amount, Max amount");
		System.out.println(addon.getName() + ", " + addon.getCategory().getCode() + ", " + addon.getMinimalAmount() + ", " + addon.getAmount() + ", " + addon.getMaximalAmount());
		while (true) {
			String string = Choices.getInput("New items: ");
			if (string != null) {
				int amount;
				try
				{
					amount = Integer.parseInt(string);
				} catch (NumberFormatException ex)
				{
					System.out.println("This is not a number. Try it again");
					continue;
				}
				amount += addon.getAmount();
				if (amount >= 0 && amount <= addon.getMaximalAmount()) {
					addon.setAmount(amount);
					break;
				} else {
					System.out.println("Your number is too large. Try it again");
				}
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}
		updateAddon(addon);
	}
	
	private void handOver() {
		AddonDelivery addonDelivery = new AddonDelivery();
		Category category = findCategoryByCode();
		filtredListAllAddons(category);
		Addon addon = addonMenu();
		System.out.println("Name, Category, Min Amount, Amount, Max amount");
		System.out.println(addon.getName() + ", " + addon.getCategory().getCode() + ", " + addon.getMinimalAmount() + ", " + addon.getAmount() + ", " + addon.getMaximalAmount());
		while (true) {
			String string = Choices.getInput("Number of items: ");
			if (string != null) {
				int amount;
				try
				{
					amount = Integer.parseInt(string);
				} catch (NumberFormatException ex)
				{
					System.out.println("This is not a number. Try it again");
					continue;
				}
				addonDelivery.setAmount(amount);
				amount = addon.getAmount() - amount;
				if (amount >= 0 && amount <= addon.getMaximalAmount()) {
					addon.setAmount(amount);
					break;
				} else {
					System.out.println("Your number is too large. Try it again");
				}
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}
		updateAddon(addon);
		addonDelivery.setCategory(addon.getCategory());

		while (true) {
			String string = Choices.getInput("Customer name: " );
			Pattern pattern = Pattern.compile(".{2,100}");
			Matcher matcher = pattern.matcher(string);
			if (matcher.matches()) {
				addonDelivery.setCustomerName(string);
				break;
			} else {
				System.out.println("The Name in not valid. Try it again");
			}
		}
		Session session = sessionFactory.openSession();
		session.beginTransaction();
		session.update(addonDelivery);
		session.getTransaction().commit();
		session.close();
		System.out.println("The Delivery was expedited");
	}

	private void updateAddon(Addon addon) {
		Session session = sessionFactory.openSession();

		// Begin a transaction
		session.beginTransaction();

		// Use the session to update the contact
		session.update(addon);

		// Commit the transaction
		session.getTransaction().commit();

		// Close the session
		session.close();
		System.out.println("The addon was updated");
	}

	/**
	 * Show list of addons
	 *
	 * @return Addon that has been selected
	 */
	private static Addon addonMenu() {
		Addon addon;

		while (true) {
			long id;
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

	public List<Category> showCategoryList() {
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
		return categories;
	}

	public Category findCategoryByCode() {
		List<Category> categories = showCategoryList();

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

	public Category findCategoryByCode(Category previousCategory) {
		List<Category> categories = showCategoryList();

		Category foundCategory = null;
		while (true) {
			boolean isFounded = false;
			String categoryCode = Choices.getInput("Enter Code of the Category (" + previousCategory.getCode() + "): ");
			if (categoryCode.length() == 0) {
				foundCategory = previousCategory;
				break;
			}
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
