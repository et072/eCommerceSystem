package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import model.Cart;
import model.Client;
import model.Item;
import model.Order;

public class OrdersDAOImpl implements OrdersDAO{
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
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean createOrder(Cart cart, String clientID) {
		boolean result = false;
		
		List<Item> items = cart.getItems();
		
		String uniqueID = UUID.randomUUID().toString();		// Randomly generated alphanumeric UUID
		String date = LocalDateTime.now().toString();		// Current date and time
		
		// Query that checks if an order ID already exists in the database
		String sql = "SELECT orderID FROM orders WHERE orderID = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, uniqueID);
			
			// Execute query
			ResultSet resultSet = ps.executeQuery();
			
			// Continue looping until a unique UUID is generated
			while(resultSet.next()) {
				resultSet.close();		// Close the current result set
				
				uniqueID = UUID.randomUUID().toString();	// Create a new order ID
				ps.setString(1, uniqueID);		// Set the new order ID
				
				resultSet = ps.executeQuery();		// Store the results of the new query to check again for duplicate order IDs
				
				System.out.println("UUID already exists");
			}
			
			// Statement to insert a new order record (order ID, client who ordered, total price, and date of order)
			// Used to associate client's with orders, as well as the time of their order
			sql = "INSERT INTO orders (orderID, clientID, date, total) VALUES (?, ?, ?, ?)";
			
			// Create prepared statement
			ps = connection.prepareStatement(sql);
			ps.setString(1, uniqueID);			// Order ID
			ps.setLong(2, Long.parseLong(clientID));		// Client ID
			ps.setString(3, date);			// Date
			ps.setDouble(4, cart.getTotalPrice());		// Total price of order
			
			ps.executeUpdate();		// Update database with new order record
			
			// Statement to insert a new order record (product ID, order ID, quantity of products ordered)
			// Used to track quantity of products ordered based on their order ID
			sql = "INSERT INTO order_product (productID, order_ID, quantity) VALUES (?, ?, ?)";
			
			// Create prepared statement
			ps = connection.prepareStatement(sql);
			
			// For every product in the cart
			for (Item item : items) {
				// Set the information that will be passed to the database
				ps.setLong(1, item.getItemID());		// Product ID
				ps.setString(2, uniqueID);			// Order ID
				ps.setLong(3, item.getQuantity());		// Quantity

				ps.executeUpdate();		// Update database with new order record
			}
			
			result = true;
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
	public List<Order> getOrders(String clientID, ClientsDAO clientDAO, ItemsDAO itemDAO) {
		List<Order> orders = new ArrayList<>();			// List of orders
		
		// Get permission level of current client
		// If they are an admin, give them all the order records
		// If they are a customer, only provide them with their own order records
		String status = clientDAO.getStatus(clientID);
		
		// Query to get order records 
		String sql1 = "";
		
		// Query to get products from an order
		String sql2 = "SELECT * FROM order_product WHERE order_ID = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = null;
			
			// If the user is an admin
			if (status.equalsIgnoreCase("admin")) {
				// Query to get all order records
				sql1 = "SELECT * FROM orders";
				
				// Prepare statement
				ps = connection.prepareStatement(sql1);
			}
			
			// If user is a customer
			else if (status.equalsIgnoreCase("client")) {
				// Query to get users order records
				sql1 = "SELECT * FROM orders WHERE clientID = ?";
				ps = connection.prepareStatement(sql1);
				ps.setLong(1, Long.parseLong(clientID));
			}
			
			// User permission level is not valid.
			else {
				throw new Exception("Incorrect user status");
			}
			
			ResultSet resultSet = ps.executeQuery();		// Execute query to get order records
			
			// Go through every order record
			while (resultSet.next()) {
				// New order object
				Order order = new Order();
				
				// Set order object parameters
				order.setOrderID(resultSet.getString("orderID"));
				order.setClientID(resultSet.getLong("clientID"));
				order.setDate(resultSet.getString("date"));
				order.setTotalPrice(resultSet.getDouble("total"));
				
				// Get user's full name and set it in the order object
				Client client = clientDAO.login(resultSet.getString("clientID"));
				String clientName = client.getFirstName() + " " + client.getLastName();
				order.setClientFullName(clientName);
				
				List<Item> items = new ArrayList<>();
				
				// Get all products in current order
				ps = connection.prepareStatement(sql2);
				ps.setString(1, resultSet.getString("orderID"));
				
				ResultSet products = ps.executeQuery();
				
				// Go through all products in the current order
				while (products.next()) {
					// Set item object
					Item item = itemDAO.getItem(products.getLong("productID"));
					item.setQuantity(products.getInt("quantity"));
					
					items.add(item);		// Add product to list of products
				}
				
				order.setItems(items);		// Set all products into order object
				
				orders.add(order);			// Add current order to list of orders
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return orders;
	}
	
	
}
