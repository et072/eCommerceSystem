package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Category;
import model.Item;

public class ItemsDAOImpl implements ItemsDAO {
	private String path;
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");		// Dynamically load vendor specific driver class
		}
		catch(ClassNotFoundException e){
		}
	}
	
	// Establishes connection with the database
	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(String.format("jdbc:sqlite:%s", this.path));
	}
	
	// Close the existing connection with the database
	private void closeConnection(Connection connection) {
		if (connection == null)
			return;
		try {
			connection.close();
		}
		catch(SQLException e) {
		}
	}
	
	@Override
	public List<Item> findAllItems() {
		List<Item> result = new ArrayList<Item>();		// Holds the list of products
		
		// Query to get all products and their information from the database
		// Inner join used to match category name with item category ID.
		String sql = "SELECT Item.itemID, Item.categoryID, Item.name, Item.description, Item.brand, Item.quantity, Item.price, Category.Category FROM Item INNER JOIN Category ON Item.categoryID = Category.ID";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			
			// Store data from executing query
			ResultSet resultSet = ps.executeQuery();
			
			// Go through the data returned from the database and add all products to a list
			while (resultSet.next()){
				// Get information of current product
				Long id = resultSet.getLong("itemID");
				int categoryID = resultSet.getInt("categoryID");
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String brand = resultSet.getString("brand");
				int quantity = resultSet.getInt("quantity");
				int price = resultSet.getInt("price");
				
				// Initialize product object and set its values
				Item item = new Item();
				item.setBrand(brand);
				item.setCategoryID(categoryID);
				item.setDescription(description);
				item.setItemID(id);
				item.setName(name);
				item.setPrice(price);
				item.setQuantity(quantity);
				
				result.add(item);		// Add product to list that will be returned
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return result;
	}

	@Override
	public List<Item> searchItemsByKeyword(String keyWord) {
		List<Item> result = new ArrayList<Item>();
		
		// Trim keyword search to remove preceding and trailing whitespace. Added wildcard to find similar results.
		String keyWordTrimmed = "%" + keyWord.trim() + "%";
		
		// Query that finds products based on keyword search
		String sql = "SELECT * FROM Item INNER JOIN Category ON Item.categoryId = Category.id"
				+ " WHERE name LIKE ? OR description LIKE ? OR Category LIKE ? OR brand LIKE ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, keyWordTrimmed);
			ps.setString(2, keyWordTrimmed);
			ps.setString(3, keyWordTrimmed);
			ps.setString(4, keyWordTrimmed);
			
			// Store data from executing query
			ResultSet resultSet = ps.executeQuery();
			
			// Go through all products
			while (resultSet.next()) {
				// Get information of current product
				Long id = resultSet.getLong("itemID");
				int categoryID = resultSet.getInt("categoryID");
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String brand = resultSet.getString("brand");
				int quantity = resultSet.getInt("quantity");
				int price = resultSet.getInt("price");
				
				// Initialize product object and set its values
				Item item = new Item();
				item.setBrand(brand);
				item.setCategoryID(categoryID);
				item.setDescription(description);
				item.setItemID(id);
				item.setName(name);
				item.setPrice(price);
				item.setQuantity(quantity);
				
				result.add(item);		// Add product to list that will be returned
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);		// Close database connection
		}

		return result;
	}

	@Override
	public List<Category> findAllCategories() {
		List<Category> result = new ArrayList<>();		// List of categories
		
		String sql = "SELECT * FROM Category";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			
			// Store data from executing query
			ResultSet resultSet = ps.executeQuery();
			
			// Go through all categories returned from the database
			while (resultSet.next()) {
				Category category = new Category();
				
				// Set category information
				category.setId(resultSet.getLong("ID"));
	            category.setCategoryDescription(resultSet.getString("Category"));
	            
				result.add(category);		// Add category to the list
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return result;
	}

	@Override
	public List<String> findAllBrands() {
		List<String> result = new ArrayList<>();
		
		// Query to get all unique product brands
		String sql = "SELECT DISTINCT Brand FROM Item";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			
			// Store data from executing query
			ResultSet resultSet = ps.executeQuery();
			
			// Go through all the product brands
			while (resultSet.next()) {
				String brand = resultSet.getString("brand");		// Get brand
				
				result.add(brand);		// Add brand to list
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return result;
	}

	@Override
	public void insert(Item item) {
		// Statement to insert a product into the database
		String sql = "INSERT INTO Items (name) VALUES (?)";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			ps.setString(1, item.getName());
			
			ps.execute();		// Insert product into the database
			
			// Get the new product's item ID
			// Item IDs are automatically part of an index. We use generated keys to get the next product ID.
			ResultSet generatedKeys = ps.getGeneratedKeys();
			
			// Set the new product's item ID in the database
			if (generatedKeys.next()) {
				item.setItemID(generatedKeys.getLong(1));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public void delete(long itemId) {
		// Statement to delete a product from the database based on its ID
		String sql = "DELETE FROM items WHERE id = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, itemId);
			
			ps.execute();		// Delete product from database
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
	}

	@Override
	public List<Item> findItemsByCategory(String category) {
		List<Item> result = new ArrayList<Item>();
		
		// Query to get all products by category
		String sql = "SELECT Item.itemID, Item.categoryID, Item.name, Item.description, Item.brand, Item.quantity, Item.price, Category.Category FROM Item INNER JOIN Category ON Item.categoryID = Category.ID WHERE "
				+ "Category = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, category);
			
			// Store data from executing query
			ResultSet resultSet = ps.executeQuery();
			
			// Go through all products
			while(resultSet.next()) {
				// Get information of current product
				Long id = resultSet.getLong("itemID");
				int categoryID = resultSet.getInt("categoryID");
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				String brand = resultSet.getString("brand");
				int quantity = resultSet.getInt("quantity");
				int price = resultSet.getInt("price");
				
				// Initialize product object and set its values
				Item item = new Item();
				item.setBrand(brand);
				item.setCategoryID(categoryID);
				item.setDescription(description);
				item.setItemID(id);
				item.setName(name);
				item.setPrice(price);
				item.setQuantity(quantity);
				
				result.add(item);		// Add product to list that will be returned
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return result;
	}

	@Override
	public List<Item> findItemsByBrand(String brand) {
		List<Item> result = new ArrayList<Item>();
		
		// Query to get products by a brand name
		String sql = "SELECT Item.itemID, Item.categoryID, Item.name, Item.description, Item.brand, Item.quantity, Item.price, Category.Category FROM Item INNER JOIN Category ON Item.categoryID = Category.ID WHERE "
				+ "brand = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, brand);
			
			// Store data from executing query
			ResultSet resultSet = ps.executeQuery();
			
			// Go through all products
			while (resultSet.next()) {
				// Get information of current product
				Long id = resultSet.getLong("itemID");
				int categoryID = resultSet.getInt("categoryID");
				String name = resultSet.getString("name");
				String description = resultSet.getString("description");
				int quantity = resultSet.getInt("quantity");
				int price = resultSet.getInt("price");
				
				// Initialize product object and set its values
				Item item = new Item();
				item.setBrand(brand);
				item.setCategoryID(categoryID);
				item.setDescription(description);
				item.setItemID(id);
				item.setName(name);
				item.setPrice(price);
				item.setQuantity(quantity);
				
				result.add(item);		// Add product to list
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return result;
	}

	@Override
	public Item getItem(long id) {
		// Query to get a specific product by its ID
		String sql = "SELECT Item.itemID, Item.categoryID, Item.name, Item.description, Item.brand, Item.quantity, Item.price FROM Item WHERE "
				+ "itemId = ?";
		
		Item item = new Item();
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setLong(1, id);
			
			// Store data from executing query
			ResultSet resultSet = ps.executeQuery();
			
			// Get all the product information and set it for the product object
			while (resultSet.next()) {
				item.setItemID(id);
				item.setCategoryID(resultSet.getInt("categoryID"));
				item.setName(resultSet.getString("name"));
				item.setDescription(resultSet.getString("description"));
				item.setBrand(resultSet.getString("brand"));
				item.setPrice(resultSet.getInt("price"));
				item.setQuantity(resultSet.getInt("quantity"));
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return item;
	}

}
