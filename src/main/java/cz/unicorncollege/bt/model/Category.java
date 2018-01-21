package cz.unicorncollege.bt.model;

import javax.persistence.*;

@Entity
public class Category {
	@Id
	@Column
	private String code;
	@Column
	private String name;

	public Category() {}

	public String getCode() {
		return code;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
}
