package com.onlinefoodorderapplication.model;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

public class Dish {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@Size(min = 3, max = 10)
	private String name;

	@NotNull
	@Size(min = 3, max = 100)
	private String ingredients;

	@NotNull
	private int restaurantId;

	public Dish() {
		super();
	}

	public Dish(int id, @NotNull @Size(min = 3, max = 10) String name,
			@NotNull @Size(min = 3, max = 100) String ingredients, @NotNull int restaurantId) {
		super();
		this.id = id;
		this.name = name;
		this.ingredients = ingredients;
		this.restaurantId = restaurantId;
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

	public int getRestaurantId() {
		return restaurantId;
	}

	public void setRestaurantId(int restaurantId) {
		this.restaurantId = restaurantId;
	}

	public String getIngredients() {
		return ingredients;
	}

	public void setIngredients(String ingredients) {
		this.ingredients = ingredients;
	}

	@Override
	public String toString() {
		return "Dish [id=" + id + ", name=" + name + ", ingredients=" + ingredients + "]";
	}

}
