package com.onlinefoodorderapplication.model;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Restaurant {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@Size(min = 3, max = 20)
	private String name;

	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Dish> dishes;

	@NotNull
	@Size(min = 3, max = 10)
	private String address;

	public Restaurant() {
		super();
	}

	public Restaurant(int id, @NotNull @Size(min = 3, max = 10) String name, List<Dish> dishes,
			@NotNull @Size(min = 3, max = 10) String address) {
		super();
		this.id = id;
		this.name = name;
		this.dishes = dishes;
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Dish> getDishes() {
		return dishes;
	}

	public void setDishes(List<Dish> dishes) {
		this.dishes = dishes;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", dishes=" + dishes + ", address=" + address + "]";
	}

}
