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

public class OrdersDAOImpl implements OrdersDAO {

	private String path;

	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} 
		catch (ClassNotFoundException ex) {
		}
	}

	private Connection getConnection() throws SQLException {
		return DriverManager.getConnection(String.format("jdbc:sqlite:%s", this.path));
	}

	private void closeConnection(Connection connection) {
		if (connection == null)
			return;
		try {
			connection.close();
		} 
		catch (SQLException ex) {
		}
	}

	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public boolean createOrder(Cart cart, String clientID) {
		
		// Generate alphanumeric UUID and store in sql as text
		// Check that it's not already in the sql server
		// If not then proceed with this strategy: 
		
		boolean result = false;

		List<Item> items = cart.getItems();
		
		String uniqueID = UUID.randomUUID().toString();
		String date = LocalDateTime.now().toString();
		
		
		Connection connection = null;
		try {
			// Establish connection
			connection = getConnection();
			
			// Prepare statement
			// Check orderID does not already exist
			PreparedStatement statement = connection.prepareStatement("select orderID from orders where orderID = ?");
			statement.setString(1, uniqueID);
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				resultSet.close();
				uniqueID = UUID.randomUUID().toString();
				statement.setString(1, uniqueID);
				resultSet = statement.executeQuery();
				System.out.println("UUID already exists");
			}
			
			closeConnection(connection);
			
			// Create an order
			connection = getConnection();
			
			String sql = "insert into \"orders\" (orderID, clientID, date, total) values (?, ?, ?, ?)";
			statement = connection.prepareStatement(sql);
			
			statement.setString(1, uniqueID);
			statement.setLong(2, Long.parseLong(clientID));
			statement.setString(3, date);
			statement.setDouble(4, cart.getTotalPrice());
			
			statement.executeUpdate();
			
			closeConnection(connection);
			
			// Add items to order 
			connection = getConnection();
			
			String sq2 = "insert into \"order_product\" (productID, order_ID, quantity) values (?, ?, ?)";
			statement = connection.prepareStatement(sq2);
			
			for (Item item : items) {
				
				statement.setLong(1, item.getItemID());
				statement.setString(2, uniqueID);
				statement.setLong(3, item.getQuantity());

				statement.executeUpdate();
			}
			
			result = true;
			
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return result;
	}

	@Override
	public List<Order> getOrders(String clientID, ClientsDAO clientDao, ItemsDAO itemsDao) {
		List<Order> orders = new ArrayList<>();
		
		String status = clientDao.getStatus(clientID);
		
		Connection connection = null;
		try {
			// Establish connection
			connection = getConnection();
			
			// Queries
			String sq1 = "";
			
			// Prepare statement
			PreparedStatement statement = null;
			
			if (status.equalsIgnoreCase("admin")) {	// If admin, give all orders
				sq1 = "select * from orders";
				statement = connection.prepareStatement(sq1);
			} 
			else if (status.equalsIgnoreCase("client")) {		// If customer, then specify orders of that id
				sq1 = "select * from orders where clientID = ?";
				statement = connection.prepareStatement(sq1);
				statement.setLong(1, Long.parseLong(clientID));
			} 
			else {
				throw new Exception("incorrect status");
			}
			
			ResultSet resultSet =  statement.executeQuery();
			
			while (resultSet.next()) {
				
				Order order = new Order();
				
				order.setOrderID(resultSet.getString("orderID"));
				order.setClientID(resultSet.getLong("clientID"));
				order.setDate(resultSet.getString("date"));
				order.setTotalPrice(resultSet.getDouble("total"));
				
				// Set client name
				Client client = clientDao.login(resultSet.getString("clientID"));
				String clientName = client.getFirstName() + " " + client.getLastName();
				order.setClientFullName(clientName);
				
				List<Item> items = new ArrayList<>();
				
				String sq2 = "select * from \"order_product\" where order_ID = ?";
				
				statement = connection.prepareStatement(sq2);
				statement.setString(1, resultSet.getString("orderID"));
				
				ResultSet products = statement.executeQuery();
				
				while (products.next()) {
					Item item = itemsDao.getItem(products.getLong("productID"));
					item.setQuantity(products.getInt("quantity"));
					
					items.add(item);
				}				
				
				order.setItems(items);

				orders.add(order);
			}
			
		}
		catch (SQLException ex) {
			ex.printStackTrace();
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
