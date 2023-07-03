package com.onlinefoodorderapplication.repository;

import java.util.List;

import javax.validation.Valid;

import com.onlinefoodorderapplication.model.Restaurant;

public interface RestaurantDAO {
	void createRestaurant(@Valid Restaurant restaurant);

	Restaurant getRestaurantById(int id);

	List<Restaurant> getAllRestaurants();

	void updateRestaurant(@Valid Restaurant restaurant);

	boolean deleteRestaurant(int id);

	List<Restaurant> searchRestaurantsByName(String name);

	List<Restaurant> searchRestaurantsByLocation(String location);

	List<Restaurant> searchRestaurantsByDishName(String dishName);

	void deleteAllRestaurants();

}
