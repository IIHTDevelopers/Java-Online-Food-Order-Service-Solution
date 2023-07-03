package com.assessment.onlinefoodorderapplication.functional;

import static com.assessment.onlinefoodorderapplication.testutils.TestUtils.businessTestFile;
import static com.assessment.onlinefoodorderapplication.testutils.TestUtils.currentTest;
import static com.assessment.onlinefoodorderapplication.testutils.TestUtils.testReport;
import static com.assessment.onlinefoodorderapplication.testutils.TestUtils.yakshaAssert;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.jboss.jandex.Main;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.stereotype.Component;

import com.assessment.onlinefoodorderapplication.testutils.MasterData;
import com.onlinefoodorderapplication.OnlineFoodOrderApplication;
import com.onlinefoodorderapplication.model.Dish;
import com.onlinefoodorderapplication.model.Restaurant;
import com.onlinefoodorderapplication.repository.DishDAO;
import com.onlinefoodorderapplication.repository.DishDAOImpl;
import com.onlinefoodorderapplication.repository.RestaurantDAO;
import com.onlinefoodorderapplication.repository.RestaurantDAOImpl;

@Component
public class FunctionalTests {

	private DishDAO dishDAO;
	private RestaurantDAO restaurantDAO;

	@BeforeEach
	public void setUp() {
		OnlineFoodOrderApplication mainObj = new OnlineFoodOrderApplication();
		Properties properties = new Properties();

		try (InputStream inputStream = Main.class.getClassLoader().getResourceAsStream("application.properties")) {
			properties.load(inputStream);

			String url = properties.getProperty("jdbc.url");
			String username = properties.getProperty("jdbc.username");
			String password = properties.getProperty("jdbc.password");

			mainObj.createDatabaseIfNotExists(url, username, password);
			mainObj.createDatabaseAndTablesIfNotExists(url, username, password);

			dishDAO = new DishDAOImpl(url, username, password);
			restaurantDAO = new RestaurantDAOImpl(url, username, password);
			dishDAO.removeAllDishes();
			restaurantDAO.deleteAllRestaurants();

		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@AfterAll
	public static void afterAll() {
		testReport();
	}

	@Test
	public void testCreateDish() throws IOException {
		Restaurant restaurant = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		restaurantDAO.createRestaurant(restaurant);
		List<Restaurant> createdRestaurants = restaurantDAO.getAllRestaurants();
		Dish dish;
		if (createdRestaurants.size() > 0) {
			dish = new Dish(1, "Burger", "Bun, Patty, Lettuce, Tomato, Cheese", createdRestaurants.get(0).getId());
		} else {
			dish = new Dish(1, "Burger", "Bun, Patty, Lettuce, Tomato, Cheese", 1);
		}

//		Dish dish = new Dish(0, "Pizza", "Dough, Cheese, Tomato Sauce", 1);
		dishDAO.createDish(dish);

		Dish retrievedDish = dishDAO.getDishById(dish.getId());
		try {
			yakshaAssert(currentTest(), dish.getName().equals(retrievedDish.getName()) ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetDishById() throws IOException {
		Restaurant restaurant = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		restaurantDAO.createRestaurant(restaurant);
		List<Restaurant> createdRestaurants = restaurantDAO.getAllRestaurants();
		Dish dish;
		if (createdRestaurants.size() > 0) {
			dish = new Dish(1, "Burger", "Bun, Patty, Lettuce, Tomato, Cheese", createdRestaurants.get(0).getId());
		} else {
			dish = new Dish(1, "Burger", "Bun, Patty, Lettuce, Tomato, Cheese", 1);
		}
		dishDAO.createDish(dish);

		Dish retrievedDish = dishDAO.getDishById(dish.getId());
		try {
			yakshaAssert(currentTest(), retrievedDish != null && dish.getName().equals(retrievedDish.getName()),
					businessTestFile);
		} catch (IOException e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetAllDishes() throws IOException {

		Restaurant restaurant = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		restaurantDAO.createRestaurant(restaurant);
		List<Restaurant> createdRestaurants = restaurantDAO.getAllRestaurants();
		Dish dish1;
		Dish dish2;
		if (createdRestaurants.size() > 0) {
			dish1 = new Dish(1, "Pasta", "Pasta, Sauce, Cheese", createdRestaurants.get(0).getId());
			dish2 = new Dish(2, "Salad", "Lettuce, Tomato, Cucumber", createdRestaurants.get(0).getId());
		} else {
			dish1 = new Dish(1, "Pasta", "Pasta, Sauce, Cheese", 1);
			dish2 = new Dish(2, "Salad", "Lettuce, Tomato, Cucumber", 1);
		}

		dishDAO.createDish(dish1);
		dishDAO.createDish(dish2);

		List<Dish> dishes = dishDAO.getAllDishes();
		try {
			yakshaAssert(currentTest(),
					dishes.get(0).getName().equals(dish1.getName()) && dishes.get(1).getName().equals(dish2.getName())
							? true
							: false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSearchDishesByIngredients() throws IOException {
		Restaurant restaurant = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		restaurantDAO.createRestaurant(restaurant);
		List<Restaurant> createdRestaurants = restaurantDAO.getAllRestaurants();
		Dish dish1;
		Dish dish2;
		if (createdRestaurants.size() > 0) {
			dish1 = new Dish(1, "Fried Rice", "Rice, Vegetables, Soy Sauce", createdRestaurants.get(0).getId());
			dish2 = new Dish(2, "Stir-Fried", "Noodles, Vegetables, Sauce", createdRestaurants.get(0).getId());
		} else {
			dish1 = new Dish(1, "Fried Rice", "Rice, Vegetables, Soy Sauce", 1);
			dish2 = new Dish(2, "Stir-Fried", "Noodles, Vegetables, Sauce", 1);
		}

		dishDAO.createDish(dish1);
		dishDAO.createDish(dish2);

		List<Dish> dishes = dishDAO.searchDishesByIngredients("Vegetables");
		try {
			yakshaAssert(currentTest(),
					dishes.get(0).getName().equals(dish1.getName()) && dishes.get(1).getName().equals(dish2.getName())
							? true
							: false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateDish() throws IOException {
//		Dish dish = new Dish(1, "Pizza", "Dough, Cheese", 1);

		Restaurant restaurant = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		restaurantDAO.createRestaurant(restaurant);
		List<Restaurant> createdRestaurants = restaurantDAO.getAllRestaurants();
		Dish dish;
		if (createdRestaurants.size() > 0) {
			dish = new Dish(1, "Burger", "Bun, Patty, Lettuce, Tomato, Cheese", createdRestaurants.get(0).getId());
		} else {
			dish = new Dish(1, "Burger", "Bun, Patty, Lettuce, Tomato, Cheese", 1);
		}
		dishDAO.createDish(dish);

		dish.setName("Cheese");
		dish.setIngredients("Dough, Cheese");
		dishDAO.updateDish(dish);
		Dish retrievedDish = dishDAO.getDishById(dish.getId());
		try {
			yakshaAssert(currentTest(), dish.getName().equals(retrievedDish.getName()) ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteDish() throws IOException {
		Restaurant restaurant = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		restaurantDAO.createRestaurant(restaurant);
		List<Restaurant> createdRestaurants = restaurantDAO.getAllRestaurants();
		Dish dish;
		if (createdRestaurants.size() > 0) {
			dish = new Dish(1, "Burger", "Bun, Patty, Lettuce, Tomato, Cheese", createdRestaurants.get(0).getId());
		} else {
			dish = new Dish(1, "Burger", "Bun, Patty, Lettuce, Tomato, Cheese", 1);
		}
		dishDAO.createDish(dish);
		boolean isDeleted = dishDAO.deleteDish(dish.getId());
		Dish deletedDish = dishDAO.getDishById(dish.getId());
		try {
			yakshaAssert(currentTest(), deletedDish == null && isDeleted != false ? true : false, businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testCreateRestaurant() throws IOException {
		Restaurant restaurant = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		restaurantDAO.createRestaurant(restaurant);
		Restaurant retrievedRestaurant = restaurantDAO.getRestaurantById(restaurant.getId());
//		Assertions.assertEquals(restaurant, retrievedRestaurant);
		try {
			yakshaAssert(currentTest(), restaurant.getName().equals(retrievedRestaurant.getName()) ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetRestaurantById() throws IOException {
		Restaurant restaurant = new Restaurant(1, "Restaurant B", MasterData.getAllDishes(), "456");
		restaurantDAO.createRestaurant(restaurant);
		Restaurant retrievedRestaurant = restaurantDAO.getRestaurantById(restaurant.getId());
//		Assertions.assertEquals(restaurant, retrievedRestaurant);
		try {
			yakshaAssert(currentTest(), restaurant.getName().equals(retrievedRestaurant.getName()) ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testGetAllRestaurants() throws IOException {
		Restaurant restaurant1 = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		Restaurant restaurant2 = new Restaurant(2, "Restaurant B", MasterData.getAllDishes(), "456");
		restaurantDAO.createRestaurant(restaurant1);
		restaurantDAO.createRestaurant(restaurant2);
		List<Restaurant> restaurants = restaurantDAO.getAllRestaurants();
//		Assertions.assertEquals(2, restaurants.size());
//		Assertions.assertTrue(restaurants.contains(restaurant1));
//		Assertions.assertTrue(restaurants.contains(restaurant2));
		try {
			yakshaAssert(currentTest(), restaurants.get(0).getName().equals(restaurant1.getName()) ? true : false,
					businessTestFile);
			yakshaAssert(currentTest(), restaurants.get(1).getName().equals(restaurant2.getName()) ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testUpdateRestaurant() throws IOException {
		Restaurant restaurant = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		restaurantDAO.createRestaurant(restaurant);
		restaurant.setName("Updated Restaurant A");
		restaurantDAO.updateRestaurant(restaurant);
		Restaurant updatedRestaurant = restaurantDAO.getRestaurantById(restaurant.getId());
//		Assertions.assertEquals("Updated Restaurant A", updatedRestaurant.getName());
		try {
			yakshaAssert(currentTest(), updatedRestaurant.getName().equals("Updated Restaurant A") ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testDeleteRestaurant() throws IOException {
		Restaurant restaurant = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		restaurantDAO.createRestaurant(restaurant);
		boolean isDeleted = restaurantDAO.deleteRestaurant(restaurant.getId());
		Restaurant deletedRestaurant = restaurantDAO.getRestaurantById(restaurant.getId());
//		Assertions.assertNull(deletedRestaurant);
		try {
			yakshaAssert(currentTest(), isDeleted == true && deletedRestaurant == null ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSearchRestaurantsByName() throws IOException {
		Restaurant restaurant1 = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		Restaurant restaurant2 = new Restaurant(2, "Restaurant B", MasterData.getAllDishes(), "124");
		restaurantDAO.createRestaurant(restaurant1);
		restaurantDAO.createRestaurant(restaurant2);
		List<Restaurant> restaurants = restaurantDAO.searchRestaurantsByName("Restaurant A");
//		Assertions.assertEquals(1, restaurants.size());
//		Assertions.assertEquals(restaurant1, restaurants.get(0));
		try {
			yakshaAssert(currentTest(), restaurants.size() == 1 ? true : false, businessTestFile);
			yakshaAssert(currentTest(), restaurants.get(0).getName().equals(restaurant1.getName()) ? true : false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSearchRestaurantsByLocation() throws IOException {
		Restaurant restaurant1 = new Restaurant(1, "Restaurant A", MasterData.getAllDishes(), "123");
		Restaurant restaurant2 = new Restaurant(2, "Restaurant B", MasterData.getAllDishes(), "456");
		restaurantDAO.createRestaurant(restaurant1);
		restaurantDAO.createRestaurant(restaurant2);
		List<Restaurant> restaurants = restaurantDAO.searchRestaurantsByLocation("456");
//		Assertions.assertEquals(1, restaurants.size());
//		Assertions.assertEquals(restaurant2, restaurants.get(0));
		try {
			yakshaAssert(currentTest(),
					restaurants.size() == 1 && restaurants.get(0).getName().equals(restaurant2.getName()) ? true
							: false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}

	@Test
	public void testSearchRestaurantsByDishName() throws IOException {
		Dish dish1 = new Dish(1, "Dish A", "Ingredients for Dish A", 1);

		List<Dish> dishes = new ArrayList<>();
		dishes.add(dish1);

		Restaurant restaurant1 = new Restaurant(1, "Restaurant A", dishes, "123");
		restaurant1.getDishes().add(dish1);
		restaurantDAO.createRestaurant(restaurant1);

		Dish dish2 = new Dish(2, "Dish B", "Ingredients for Dish B", 2);
		List<Dish> dishes2 = new ArrayList<>();
		dishes2.add(dish2);

		Restaurant restaurant2 = new Restaurant(2, "Restaurant B", dishes2, "456");
		restaurant2.getDishes().add(dish2);

		restaurantDAO.createRestaurant(restaurant2);

		List<Restaurant> createdRestaurants = restaurantDAO.getAllRestaurants();
		dish1 = new Dish(1, "Dish A", "Ingredients for Dish A", createdRestaurants.get(0).getId());
		dishDAO.createDish(dish1);
		dish2 = new Dish(2, "Dish B", "Ingredients for Dish B", createdRestaurants.get(1).getId());
		dishDAO.createDish(dish2);
		List<Restaurant> restaurants = restaurantDAO.searchRestaurantsByDishName("Dish A");

		try {
			yakshaAssert(currentTest(),
					restaurants.size() == 1
							&& restaurants.get(0).getDishes().stream().anyMatch(dish -> dish.getName().equals("Dish A"))
									? true
									: false,
					businessTestFile);
		} catch (Exception e) {
			yakshaAssert(currentTest(), false, businessTestFile);
		}
	}
}
