package cz.unicorncollege.bt.model;

import javax.persistence.*;

@Entity
public class Addon {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn
	private Category category;

	@Column
	private String name;

	@Column
	private int amount;

	@Column
	private int maximalAmount = 1000;

	@Column
	private boolean isDeleted = false;

	public Addon(String category, String name, int amount, int maximalAmount){}

	public Addon() {}

	@Override
	public String toString() {
		return "Addon{" +
				"id=" + id +
				", category=" + category +
				", name='" + name + '\'' +
				", amount=" + amount +
				", maximalAmount=" + maximalAmount +
				'}';
	}

	//public static class ContactBuilder {

//	public AddonBuilder(String firstName, String lastName) {
//		this.firstName = firstName;
//		this.lastName = lastName;
//	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public int getMaximalAmount() {
		return maximalAmount;
	}

	public void setMaximalAmount(int maximalAmount) {
		this.maximalAmount = maximalAmount;
	}

	public boolean isDeleted() {
		return isDeleted;
	}

	public void setDeleted(boolean deleted) {
		isDeleted = deleted;
	}
}
