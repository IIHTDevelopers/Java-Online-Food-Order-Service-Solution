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

public class DishDAOImpl implements DishDAO {

	private final String url;
	private final String username;
	private final String password;

	public DishDAOImpl(String url, String username, String password) {
		this.url = url;
		this.username = username;
		this.password = password;
	}

	@Override
	public void createDish(Dish dish) {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String createDishQuery = "INSERT INTO dish (name, ingredients, restaurant_id) VALUES (?, ?, ?)";

			PreparedStatement preparedStatement = connection.prepareStatement(createDishQuery,
					Statement.RETURN_GENERATED_KEYS);
			preparedStatement.setString(1, dish.getName());
			preparedStatement.setString(2, dish.getIngredients());
			preparedStatement.setInt(3, dish.getRestaurantId());

			int rowsAffected = preparedStatement.executeUpdate();

			if (rowsAffected == 1) {
				ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
				if (generatedKeys.next()) {
					int dishId = generatedKeys.getInt(1);
					dish.setId(dishId);
				}
				generatedKeys.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Dish getDishById(int id) {
		String query = "SELECT * FROM dish WHERE id = ?";

		try (Connection connection = DriverManager.getConnection(url, username, password);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setInt(1, id);

			try (ResultSet resultSet = statement.executeQuery()) {
				if (resultSet.next()) {
					String name = resultSet.getString("name");
					String ingredients = resultSet.getString("ingredients");

					Dish dish = new Dish();
					dish.setId(id);
					dish.setName(name);
					dish.setIngredients(ingredients);

					return dish;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	@Override
	public List<Dish> getAllDishes() {
		String query = "SELECT * FROM dish";
		List<Dish> dishes = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(url, username, password);
				Statement statement = connection.createStatement();
				ResultSet resultSet = statement.executeQuery(query)) {
			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String name = resultSet.getString("name");
				String ingredients = resultSet.getString("ingredients");

				Dish dish = new Dish();
				dish.setId(id);
				dish.setName(name);
				dish.setIngredients(ingredients);

				dishes.add(dish);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dishes;
	}

	@Override
	public void updateDish(Dish dish) {
		String query = "UPDATE dish SET name = ?, ingredients = ? WHERE id = ?";

		try (Connection connection = DriverManager.getConnection(url, username, password);
				PreparedStatement statement = connection.prepareStatement(query)) {
			statement.setString(1, dish.getName());
			statement.setString(2, dish.getIngredients());
			statement.setInt(3, dish.getId());

			statement.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public boolean deleteDish(int id) {
		String query = "DELETE FROM dish WHERE id = ?";

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

	@Override
	public List<Dish> searchDishesByIngredients(String ingredients) {
		List<Dish> dishes = new ArrayList<>();

		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String query = "SELECT * FROM dish WHERE ingredients LIKE ?";

			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, "%" + ingredients + "%");

			ResultSet resultSet = preparedStatement.executeQuery();

			while (resultSet.next()) {
				int id = resultSet.getInt("id");
				String dishName = resultSet.getString("name");
				String dishIngredients = resultSet.getString("ingredients");
				int restaurantId = resultSet.getInt("restaurant_id");

				Dish dish = new Dish(id, dishName, dishIngredients, restaurantId);
				dishes.add(dish);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return dishes;
	}

	@Override
	public void removeAllDishes() {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String query = "DELETE FROM dish";

			Statement statement = connection.createStatement();
			statement.executeUpdate(query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
