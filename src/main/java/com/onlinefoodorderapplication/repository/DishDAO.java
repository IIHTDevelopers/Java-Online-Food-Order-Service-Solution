package com.onlinefoodorderapplication.repository;

import java.util.List;

import javax.validation.Valid;

import com.onlinefoodorderapplication.model.Dish;

public interface DishDAO {
	void createDish(@Valid Dish dish);

	Dish getDishById(int id);

	List<Dish> getAllDishes();

	void updateDish(@Valid Dish dish);

	boolean deleteDish(int id);

	List<Dish> searchDishesByIngredients(String ingredients);

	void removeAllDishes();

}
