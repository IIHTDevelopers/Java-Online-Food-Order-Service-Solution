package com.assessment.onlinefoodorderapplication.testutils;

import java.util.ArrayList;
import java.util.List;

import com.onlinefoodorderapplication.model.Dish;
import com.onlinefoodorderapplication.model.Restaurant;

public class MasterData {
	public static Dish getDish() {
		Dish dish = new Dish();
		dish.setId(1);
		dish.setIngredients("x y z");
		dish.setName("abc");
		dish.setRestaurantId(1);
		return dish;
	}

	public static List<Dish> getAllDishes() {

		List<Dish> dishes = new ArrayList<>();
		Dish dish = new Dish();

		dish.setId(1);
		dish.setIngredients("x y z");
		dish.setName("abc");
		dish.setRestaurantId(1);
		dishes.add(dish);

		dish = new Dish();

		dish.setId(2);
		dish.setIngredients("p q r");
		dish.setName("xyz");
		dish.setRestaurantId(2);

		dishes.add(dish);

		return dishes;
	}

	public static Restaurant getRestaurant() {
		Restaurant restaurant = new Restaurant();
		restaurant.setId(1);
		restaurant.setName("alpha 1");
		restaurant.setDishes(getAllDishes());
		return restaurant;
	}

	public static List<Restaurant> getAllRestaurants() {

		List<Restaurant> restaurants = new ArrayList<>();

		Restaurant restaurant = new Restaurant();
		restaurant.setId(1);
		restaurant.setName("alpha 1");
		restaurant.setDishes(getAllDishes());

		restaurants.add(restaurant);

		restaurant = new Restaurant();
		restaurant.setId(2);
		restaurant.setName("temp name");
		restaurant.setDishes(getAllDishes());

		restaurants.add(restaurant);
		return restaurants;
	}
}
