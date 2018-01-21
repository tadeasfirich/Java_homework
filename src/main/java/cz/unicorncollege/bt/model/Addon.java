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
	private int minimalAmount;

	public Addon(String category, String name, int amount, int minimalAmount){}

	public Addon() {}

	@Override
	public String toString() {
		return "Addon{" +
				"id=" + id +
				", category=" + category +
				", name='" + name + '\'' +
				", amount=" + amount +
				", minimalAmount=" + minimalAmount +
				'}';
	}

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
	
	public int getMinimalAmount() {
		return minimalAmount;
	}
	
	public void setMinimalAmount(int minimalAmount) {
		this.minimalAmount = minimalAmount;
	}
}