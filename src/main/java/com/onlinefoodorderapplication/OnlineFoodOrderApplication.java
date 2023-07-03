package com.onlinefoodorderapplication;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import org.jboss.jandex.Main;

import com.onlinefoodorderapplication.model.Dish;
import com.onlinefoodorderapplication.model.Restaurant;
import com.onlinefoodorderapplication.repository.DishDAO;
import com.onlinefoodorderapplication.repository.DishDAOImpl;
import com.onlinefoodorderapplication.repository.RestaurantDAO;
import com.onlinefoodorderapplication.repository.RestaurantDAOImpl;

public class OnlineFoodOrderApplication {
	private static RestaurantDAO restaurantDAO;
	private static DishDAO dishDAO;
	private static Scanner scanner;

	public static void main(String[] args) {
		Properties properties = new Properties();

		try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
			properties.load(inputStream);

			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String password = properties.getProperty("jdbc.password");

			createDatabaseIfNotExists(url, username, password);
			createDatabaseAndTablesIfNotExists(url, username, password);

			restaurantDAO = new RestaurantDAOImpl(url, username, password);
			dishDAO = new DishDAOImpl(url, username, password);

			scanner = new Scanner(System.in);
			boolean exit = false;

			while (!exit) {
				printMenu();
				int choice = getUserChoice();

				switch (choice) {
				case 1:
					createRestaurant();
					break;
				case 2:
					createDish();
					break;
				case 3:
					updateRestaurant();
					break;
				case 4:
					updateDish();
					break;
				case 5:
					deleteRestaurant();
					break;
				case 6:
					deleteDish();
					break;
				case 7:
					getRestaurantById();
					break;
				case 8:
					getAllRestaurants();
					break;
				case 9:
					getAllDishes();
					break;
				case 10:
					searchRestaurantsByName();
					break;
				case 11:
					searchRestaurantsByLocation();
					break;
				case 12:
					searchRestaurantsByDishName();
					break;
				case 13:
					searchDishesByIngredients();
					break;
				case 14:
					removeAllDishes();
					break;
				case 15:
					deleteAllRestaurants();
					break;
				case 16:
					exit = true;
					break;
				default:
					System.out.println("Invalid choice. Please try again.");
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (scanner != null) {
				scanner.close();
			}
		}
	}

	private static void getAllDishes() {
		List<Dish> dishes = dishDAO.getAllDishes();

		if (dishes.isEmpty()) {
			System.out.println("No dishes found.");
		} else {
			System.out.println("All Dishes:");
			for (Dish dish : dishes) {
				System.out.println(dish);
			}
		}
	}

	private static void printMenu() {
		System.out.println("1. Create Restaurant");
		System.out.println("2. Create Dish");
		System.out.println("3. Update Restaurant");
		System.out.println("4. Update Dish");
		System.out.println("5. Delete Restaurant");
		System.out.println("6. Delete Dish");
		System.out.println("7. Get Restaurant by ID");
		System.out.println("8. Get All Restaurants");
		System.out.println("9. Get All Dishes");
		System.out.println("10. Search Restaurants by Name");
		System.out.println("11. Search Restaurants by Location");
		System.out.println("12. Search Restaurants by Dish Name");
		System.out.println("13. Search Dishes by Ingredients");
		System.out.println("14. Delete all Dishes");
		System.out.println("15. Delete All Restaurants");
		System.out.println("16. Exit");
	}

	private static int getUserChoice() {
		System.out.print("Enter your choice: ");
		return scanner.nextInt();
	}

	private static void createRestaurant() {
		scanner.nextLine(); // Consume newline character

		System.out.print("Enter restaurant name: ");
		String restaurantName = scanner.nextLine();
		System.out.print("Enter restaurant address: ");
		String restaurantAddress = scanner.nextLine();

		Restaurant restaurant = new Restaurant();
		restaurant.setName(restaurantName);
		restaurant.setAddress(restaurantAddress);

		restaurantDAO.createRestaurant(restaurant);
		System.out.println("Restaurant created successfully: " + restaurant);
	}

	private static void createDish() {
		scanner.nextLine(); // Consume newline character

		System.out.print("Enter dish name: ");
		String dishName = scanner.nextLine();

		System.out.print("Enter dish ingredients: ");
		String dishIngredients = scanner.nextLine();

		System.out.print("Enter restaurant ID: ");
		int restaurantId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		Restaurant restaurant = restaurantDAO.getRestaurantById(restaurantId);

		if (restaurant != null) {
			Dish dish = new Dish();
			dish.setName(dishName);
			dish.setIngredients(dishIngredients);
			dish.setRestaurantId(restaurantId);

			dishDAO.createDish(dish);
			System.out.println("Dish created successfully: " + dish);
		} else {
			System.out.println("Restaurant not found with ID: " + restaurantId);
		}
	}

	private static void updateRestaurant() {
		scanner.nextLine(); // Consume newline character

		System.out.print("Enter restaurant ID: ");
		int updateRestaurantId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		Restaurant updateRestaurant = restaurantDAO.getRestaurantById(updateRestaurantId);

		if (updateRestaurant != null) {
			System.out.print("Enter updated restaurant name: ");
			String updatedRestaurantName = scanner.nextLine();
			System.out.print("Enter updated restaurant address: ");
			String updatedRestaurantAddress = scanner.nextLine();

			updateRestaurant.setName(updatedRestaurantName);
			updateRestaurant.setAddress(updatedRestaurantAddress);

			restaurantDAO.updateRestaurant(updateRestaurant);
			System.out.println("Restaurant updated successfully: " + updateRestaurant);
		} else {
			System.out.println("Restaurant not found with ID: " + updateRestaurantId);
		}
	}

	private static void updateDish() {
		scanner.nextLine(); // Consume newline character

		System.out.print("Enter dish ID: ");
		int updateDishId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		Dish updateDish = dishDAO.getDishById(updateDishId);

		if (updateDish != null) {
			System.out.print("Enter updated dish name: ");
			String updatedDishName = scanner.nextLine();
			System.out.print("Enter updated dish ingredients: ");
			String updatedDishIngredients = scanner.nextLine();

			updateDish.setName(updatedDishName);
			updateDish.setIngredients(updatedDishIngredients);

			dishDAO.updateDish(updateDish);
			System.out.println("Dish updated successfully: " + updateDish);
		} else {
			System.out.println("Dish not found with ID: " + updateDishId);
		}
	}

	private static void deleteRestaurant() {
		System.out.print("Enter restaurant ID to delete: ");
		int deleteRestaurantId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		restaurantDAO.deleteRestaurant(deleteRestaurantId);
		System.out.println("Restaurant deleted successfully with ID: " + deleteRestaurantId);
	}

	private static void deleteDish() {
		System.out.print("Enter dish ID to delete: ");
		int deleteDishId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		dishDAO.deleteDish(deleteDishId);
		System.out.println("Dish deleted successfully with ID: " + deleteDishId);
	}

	private static void getRestaurantById() {
		System.out.print("Enter restaurant ID to retrieve: ");
		int retrieveRestaurantId = scanner.nextInt();
		scanner.nextLine(); // Consume newline character

		Restaurant retrievedRestaurant = restaurantDAO.getRestaurantById(retrieveRestaurantId);

		if (retrievedRestaurant != null) {
			System.out.println("Retrieved Restaurant: " + retrievedRestaurant);
		} else {
			System.out.println("Restaurant not found with ID: " + retrieveRestaurantId);
		}
	}

	private static void getAllRestaurants() {
		List<Restaurant> allRestaurants = restaurantDAO.getAllRestaurants();
		System.out.println("All Restaurants: " + allRestaurants);
	}

	public static void createDatabaseAndTablesIfNotExists(String url, String username, String password)
			throws SQLException {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS food_order_service";
			String createRestaurantTableQuery = "CREATE TABLE IF NOT EXISTS restaurant ("
					+ "id INT AUTO_INCREMENT PRIMARY KEY," + "name VARCHAR(20) NOT NULL,"
					+ "address VARCHAR(10) NOT NULL" + ")";
			String createDishTableQuery = "CREATE TABLE IF NOT EXISTS dish (" + "id INT AUTO_INCREMENT PRIMARY KEY,"
					+ "name VARCHAR(10) NOT NULL," + "ingredients VARCHAR(100) NOT NULL,"
					+ "restaurant_id INT NOT NULL," + "FOREIGN KEY (restaurant_id) REFERENCES restaurant(id)" + ")";

			Statement statement = connection.createStatement();
			statement.executeUpdate(createDatabaseQuery);
			statement.executeUpdate("USE food_order_service");
			statement.executeUpdate(createRestaurantTableQuery);
			statement.executeUpdate(createDishTableQuery);
		}
	}

	public static void createDatabaseIfNotExists(String url, String username, String password) throws SQLException {
		try (Connection connection = DriverManager.getConnection(url, username, password)) {
			String createDatabaseQuery = "CREATE DATABASE IF NOT EXISTS food_order_service";

			Statement statement = connection.createStatement();
			statement.executeUpdate(createDatabaseQuery);
		}
	}

	private static void searchRestaurantsByName() {
		scanner.nextLine();
		System.out.print("Enter the restaurant name to search: ");
		String name = scanner.nextLine();

		List<Restaurant> restaurants = restaurantDAO.searchRestaurantsByName(name);
		if (restaurants.isEmpty()) {
			System.out.println("No restaurants found with the given name.");
		} else {
			System.out.println("Restaurants matching the name:");
			for (Restaurant restaurant : restaurants) {
				System.out.println(restaurant);
			}
		}
	}

	private static void searchRestaurantsByLocation() {
		scanner.nextLine();
		System.out.print("Enter the location to search: ");
		String location = scanner.nextLine();

		List<Restaurant> restaurants = restaurantDAO.searchRestaurantsByLocation(location);
		if (restaurants.isEmpty()) {
			System.out.println("No restaurants found in the given location.");
		} else {
			System.out.println("Restaurants in the location:");
			for (Restaurant restaurant : restaurants) {
				System.out.println(restaurant);
			}
		}
	}

	private static void searchRestaurantsByDishName() {
		scanner.nextLine();
		System.out.print("Enter the dish name to search: ");
		String dishName = scanner.nextLine();

		List<Restaurant> restaurants = restaurantDAO.searchRestaurantsByDishName(dishName);
		if (restaurants.isEmpty()) {
			System.out.println("No restaurants found with the given dish name.");
		} else {
			System.out.println("Restaurants serving the dish:");
			for (Restaurant restaurant : restaurants) {
				System.out.println(restaurant);
			}
		}
	}

	private static void searchDishesByIngredients() {
		scanner.nextLine();
		System.out.print("Enter the ingredients to search: ");
		String ingredients = scanner.nextLine();

		List<Dish> dishes = dishDAO.searchDishesByIngredients(ingredients);
		if (dishes.isEmpty()) {
			System.out.println("No dishes found with the given ingredients.");
		} else {
			System.out.println("Dishes matching the ingredients:");
			for (Dish dish : dishes) {
				System.out.println(dish);
			}
		}
	}

	public static void removeAllDishes() {
		dishDAO.removeAllDishes();
		System.out.println("All dishes have been removed from the database.");
	}

	private static void deleteAllRestaurants() {
		restaurantDAO.deleteAllRestaurants();
		System.out.println("All restaurants have been deleted from the database.");
	}

}
