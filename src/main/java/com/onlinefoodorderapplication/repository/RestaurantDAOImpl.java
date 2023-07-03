package com.onlinefoodorderapplication.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.onlinefoodorderapplication.model.Dish;
import com.onlinefoodorderapplication.model.Restaurant;

public class RestaurantDAOImpl implements RestaurantDAO {

	private final String url;
	private final String username;
	private final String password;

	public RestaurantDAOImpl(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	public void createRestaurant(Restaurant restaurant) {
		String query = "INSERT INTO restaurant (name, address) VALUES (?, ?)";

		try (Connection connection = DriverManager.getConnection(url, username, password);
				PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
			statement.setString(1, restaurant.getName());
			statement.setString(2, restaurant.getAddress());

			int affectedRows = statement.executeUpdate();

			if (affectedRows == 0) {
				throw new SQLException("Creating restaurant failed, no rows affected.");
			}

			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
				if (generatedKeys.next()) {
					int generatedId = generatedKeys.getInt(1);
					restaurant.setId(generatedId);
				} else {
					throw new SQLException("Creating restaurant failed, no ID obtained.");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Restaurant getRestaurantById(int id) {
		String query = "SELECT * FROM restaurant WHERE id = ?";

		try (Connection connection = DriverManager.getConnection(url, username, password);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					String name = resultSet.getString("name");
					String address = resultSet.getString("address");
					List<Dish> dishes = getDishesByRestaurantId(id);

					Restaurant restaurant = new Restaurant();
					restaurant.setId(id);
					restaurant.setName(name);
					restaurant.setAddress(address);
					restaurant.setDishes(dishes);

					return restaurant;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Restaurant> getAllRestaurants() {
		String query = "SELECT * FROM restaurant";
		List<Restaurant> restaurants = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(url, username, password);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String address = resultSet.getString("address");
				List<Dish> dishes = getDishesByRestaurantId(id);

				Restaurant restaurant = new Restaurant();
				restaurant.setId(id);
				restaurant.setName(name);
				restaurant.setAddress(address);
				restaurant.setDishes(dishes);

				restaurants.add(restaurant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurants;
	}

	@Override
	public void updateRestaurant(Restaurant restaurant) {
		String query = "UPDATE restaurant SET name = ?, address = ? WHERE id = ?";

		try (Connection connection = DriverManager.getConnection(url, username, password);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, restaurant.getName());
			statement.setString(2, restaurant.getAddress());
			statement.setInt(3, restaurant.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteRestaurant(int id) {
		String query = "DELETE FROM restaurant WHERE id = ?";

		try (Connection connection = DriverManager.getConnection(url, username, password);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, id);

			statement.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	private List<Dish> getDishesByRestaurantId(int restaurantId) {
		String query = "SELECT * FROM dish WHERE id = ?";
		List<Dish> dishes = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(url, username, password);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, restaurantId);

			try (ResultSet resultSet = statement.executeQuery()) {
				while (resultSet.next()) {
					int dishId = resultSet.getInt("id");
					String name = resultSet.getString("name");
					String ingredients = resultSet.getString("ingredients");

					Dish dish = new Dish();
					dish.setId(dishId);
					dish.setName(name);
					dish.setIngredients(ingredients);

					dishes.add(dish);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dishes;
	}

	@Override
	public List<Restaurant> searchRestaurantsByName(String name) {
		List<Restaurant> restaurants = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String query = "SELECT * FROM restaurant WHERE name LIKE ?";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "%" + name + "%");

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String restaurantName = resultSet.getString("name");
				String address = resultSet.getString("address");

				List<Dish> dishes = getDishesForRestaurant(id);

				Restaurant restaurant = new Restaurant(id, restaurantName, dishes, address);
				restaurants.add(restaurant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurants;
	}

	@Override
	public List<Restaurant> searchRestaurantsByLocation(String location) {
		List<Restaurant> restaurants = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String query = "SELECT * FROM restaurant WHERE address LIKE ?";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "%" + location + "%");

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String restaurantAddress = resultSet.getString("address");

				List<Dish> dishes = getDishesForRestaurant(id);

				Restaurant restaurant = new Restaurant(id, name, dishes, restaurantAddress);
				restaurants.add(restaurant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurants;
	}

	@Override
	public List<Restaurant> searchRestaurantsByDishName(String dishName) {
		List<Restaurant> restaurants = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String query = "SELECT r.* FROM restaurant r INNER JOIN dish d ON r.id = d.restaurant_id WHERE d.name LIKE ?";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "%" + dishName + "%");

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String address = resultSet.getString("address");

				List<Dish> dishes = getDishesForRestaurant(id);

				Restaurant restaurant = new Restaurant(id, name, dishes, address);
				restaurants.add(restaurant);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return restaurants;
	}

	private List<Dish> getDishesForRestaurant(int restaurantId) {
		List<Dish> dishes = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String query = "SELECT * FROM dish WHERE restaurant_id = ?";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setInt(1, restaurantId);

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String dishName = resultSet.getString("name");
				String ingredients = resultSet.getString("ingredients");

				Dish dish = new Dish(id, dishName, ingredients, restaurantId);
				dishes.add(dish);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dishes;
	}

	@Override
	public void deleteAllRestaurants() {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String query = "DELETE FROM restaurant";

			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
