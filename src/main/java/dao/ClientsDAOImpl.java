package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.Billing;
import model.Client;
import model.Shipping;

public class ClientsDAOImpl implements ClientsDAO {
	private String path;
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");	// Dynamically load vendor specific driver class
		}
		catch (ClassNotFoundException e) {
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
		catch (SQLException e) {
		}
	}
	
	@Override
	public void setPath(String path) {
		this.path = path;
	}

	@Override
	public Client login(String username, String password) {
		Client client = null;
		
		// Query to database to check if the user login information is valid
		String sql = "SELECT * FROM client WHERE username = ? AND password = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			ps.setString(1, username);
			ps.setString(2, password);
			
			// Store data from executing query
			ResultSet resultSet = ps.executeQuery();
			
			// If data was returned from the database, login is successful
			if (resultSet.next()) {
				client = new Client();
				
				// Set up the client information
				client.setID(resultSet.getLong("ID"));
				client.setStatus(resultSet.getString("status"));
				client.setUsername(resultSet.getString("username"));
				client.setPassword(resultSet.getString("password"));
				client.setFirstName(resultSet.getString("firstName"));
				client.setLastName(resultSet.getString("lastName"));
				client.setEmail(resultSet.getString("email"));
				
				// Set up the billing information
				Billing billing = new Billing();
				
				billing.setCc(resultSet.getLong("CC"));
				billing.setCcExpiryMon(resultSet.getInt("ccExpiryMon"));
				billing.setCcExpiryYr(resultSet.getInt("ccExpiryYr"));
				billing.setCvv(resultSet.getInt("cvv"));
				billing.setAddressB(resultSet.getString("addressB"));
				billing.setProvinceB(resultSet.getString("provinceB"));
				billing.setCountryB(resultSet.getString("countryB"));
				billing.setZipB(resultSet.getString("zipB"));
				billing.setPhoneB(resultSet.getLong("phoneB"));
				
				client.setBilling(billing);		// Set the billing information for the client
				
				// Set up the shipping information
				Shipping shipping = new Shipping();
				
				shipping.setAddressS(resultSet.getString("addressS"));
				shipping.setProvinceS(resultSet.getString("provinceS"));
				shipping.setCountryS(resultSet.getString("countryS"));
				shipping.setZipS(resultSet.getString("zipS"));
				shipping.setPhoneS(resultSet.getLong("phoneS"));
				
				client.setShipping(shipping);		// Set the shipping information for the client
			}
			
			else {			// Login failed. Credentials invalid.
				return null;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);	// Close connection with database
		}
		
		return client;
	}
	
	@Override
	public Client login(String id) {
		Client client = null;
		
		// Query to get client from database based on the provided ID
		String sql = "SELECT * FROM client WHERE id = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ps.setInt(1, Integer.parseInt(id));
			
			// Get results from database after executing query
			ResultSet resultSet = ps.executeQuery();
			
			if (resultSet.next()) {
				client = new Client();
				
				// Set up the client
				client.setID(resultSet.getInt("ID"));
				client.setStatus(resultSet.getString("status"));
				client.setUsername(resultSet.getString("username"));
				client.setPassword(resultSet.getString("password"));
				client.setFirstName(resultSet.getString("firstName"));
				client.setLastName(resultSet.getString("lastName"));
				client.setEmail(resultSet.getString("email"));
				
				// Set up the billing information
				Billing billing = new Billing();

				billing.setCc(resultSet.getLong("CC"));
				billing.setCcExpiryMon(resultSet.getInt("ccExpiryMon"));
				billing.setCcExpiryYr(resultSet.getInt("ccExpiryYr"));
				billing.setCvv(resultSet.getInt("cvv"));
				billing.setAddressB(resultSet.getString("addressB"));
				billing.setProvinceB(resultSet.getString("provinceB"));
				billing.setCountryB(resultSet.getString("countryB"));
				billing.setZipB(resultSet.getString("zipB"));
				billing.setPhoneB(resultSet.getLong("phoneB"));
				
				client.setBilling(billing);		// Set the billing information for the client
				
				// Set up the shipping information
				Shipping shipping = new Shipping();

				shipping.setAddressS(resultSet.getString("addressS"));
				shipping.setProvinceS(resultSet.getString("provinceS"));
				shipping.setCountryS(resultSet.getString("countryS"));
				shipping.setZipS(resultSet.getString("zipS"));
				shipping.setPhoneS(resultSet.getLong("phoneS"));

				client.setShipping(shipping);		// Set the shipping information for the client
			}
			
			else {				// Login failed. ID invalid.
				return null;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);		// Close database connection
		}
		
		return client;
	}

	@Override
	public Client register(HttpServletRequest request) {
		Client client = null;
		
		// Create instance of client
		String sql = "INSERT INTO client (status, username, password, firstName, lastName, email, addressS, provinceS, countryS, zipS, phoneS, cc, ccExpiryMon, ccExpiryYr, cvv, addressB, provinceB, countryB, zipB, phoneB)" +
				"VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		
		Connection connection = null;
		
		try {
			// Get client info
			String status = "client";
			String username = request.getParameter("usernameR");
			String password = request.getParameter("passwordR");
			String firstName = request.getParameter("firstNameR");
			String lastName = request.getParameter("lastNameR");
			String email = request.getParameter("emailR");
			
			// Get shipping information
			String addressS = request.getParameter("addressShippingR");
			String provinceS = request.getParameter("provinceShippingR");
			String countryS = request.getParameter("countryShippingR");
			String zipS = request.getParameter("zipCodeShippingR");
			int phoneS = Integer.parseInt(request.getParameter("phoneShippingR").replaceAll("[()\\s-]+", ""));
			
			// Get billing information
			long cc = Long.parseLong(request.getParameter("creditCardR").replaceAll("[^0-9]", "")); 
			int ccExpiryMon = Integer.parseInt(request.getParameter("ccExpiryMonR").replaceAll("[^0-9]", ""));
			int ccExpiryYr = Integer.parseInt(request.getParameter("ccExpiryYrR").replaceAll("[^0-9]", ""));
			int cvv = Integer.parseInt(request.getParameter("cvvR").replaceAll("[^0-9]", ""));
			String addressB = request.getParameter("addressBillingR");
			String provinceB = request.getParameter("provinceBillingR");
			String countryB = request.getParameter("countryBillingR");
			String zipB = request.getParameter("zipCodeBillingR");
			int phoneB = Integer.parseInt(request.getParameter("phoneBillingR").replaceAll("[()\\s-]+", ""));
			
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ps.setString(1, status);
			ps.setString(2, username);
			ps.setString(3, password);
			ps.setString(4, firstName);
			ps.setString(5, lastName);
			ps.setString(6, email);
			ps.setString(7, addressS);
			ps.setString(8, provinceS);
			ps.setString(9, countryS);
			ps.setString(10, zipS);
			ps.setInt(11, phoneS);
			ps.setLong(12, cc);
			ps.setInt(13, ccExpiryMon);
			ps.setInt(14, ccExpiryYr);
			ps.setInt(15, cvv);
			ps.setString(16, addressB);
			ps.setString(17, provinceB);
			ps.setString(18, countryB);
			ps.setString(19, zipB);
			ps.setInt(20, phoneB);
			
			ps.executeUpdate();		// Update the database with the newly registered account/client
			
			client = login(username, password);		// Account registered. Log into the newly created account
		}
		catch(NumberFormatException e) {
			System.out.println("Number format exception occurred: " + e);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return client;
	}

	@Override
	public Client updateAccountInfo(String id, String username, String password, String firstName, String lastName,
			String email) {
		Client client = null;
		
		// Query to update client account information
		String sql = "UPDATE client SET username = ?, password = ?, firstName = ?, lastName = ?, email = ? WHERE id = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ps.setString(1, username);
			ps.setString(2, password);
			ps.setString(3, firstName);
			ps.setString(4, lastName);
			ps.setString(5, email);
			ps.setInt(6, Integer.parseInt(id));
			
			ps.executeUpdate();				// Update database after executing SQL statement
			
			client = login(username, password);		// Account information updated. Stay logged in as the current user.
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return client;
	}

	@Override
	public Client updateAccountShipping(String id, String address, String province, String country, String zipCode,
			String phoneNum) {
		Client client = null;
		
		// Query to update account shipping information
		String sql = "UPDATE client SET addressS = ?, provinceS = ?, countryS = ?, zipS = ?, phoneS = ? WHERE id = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ps.setString(1, address);
			ps.setString(2, province);
			ps.setString(3, country);
			ps.setString(4, zipCode);
			ps.setLong(5, Long.parseLong(phoneNum.replaceAll("\\D+", "")));
			ps.setInt(6, Integer.parseInt(id));
			
			ps.executeUpdate();				// Update database after executing SQL statement
			
			client = login(id);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return client;
	}

	@Override
	public Client updateAccountCC(String id, String cc, String expMon, String expYr, String cvv) {
		Client client = null;
		
		// Query to update payment information
		String sql = "UPDATE client SET cc = ?, ccExpiryMon = ?, ccExpiryYr = ?, cvv = ? WHERE id = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ps.setLong(1, Long.parseLong(cc));
			ps.setInt(2, Integer.parseInt(expMon));
			ps.setInt(3, Integer.parseInt(expYr));
			ps.setInt(4, Integer.parseInt(cvv));
			ps.setInt(5, Integer.parseInt(id));

			ps.executeUpdate();			// Update database after executing SQL statement
			
			client = login(id);
		}
		catch(NumberFormatException e) {
			System.out.println("Number format exception occurred: " + e);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);		// Close database connection
		}
		
		return client;
	}

	@Override
	public Client updateAccountBilling(String id, String address, String province, String country, String zipCode,
			String phoneNum) {
		Client client = null;
		
		String sql = "UPDATE client SET addressB = ?, provinceB = ?, countryB = ?, zipB = ?, phoneB = ? WHERE id = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with the database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);

			ps.setString(1, address);
			ps.setString(2, province);
			ps.setString(3, country);
			ps.setString(4, zipCode);
			ps.setLong(5, Long.parseLong(phoneNum.replaceAll("\\D+", "")));
			ps.setInt(6, Integer.parseInt(id));

			ps.executeUpdate();			// Update database after executing SQL statement
			
			client = login(id);
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return client;
	}

	@Override
	public String getStatus(String id) {
		String status = null;
		
		String sql = "SELECT status FROM client WHERE id = ?";
		
		Connection connection = null;
		
		try {
			// Establish connection with database
			connection = getConnection();
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ps.setLong(1, Long.parseLong(id));
			
			ResultSet resultSet = ps.executeQuery();
			
			resultSet.next();	// Move cursor to next position to get status
			
			status = resultSet.getString("status");		// Get status of client
			
			if (!(status.equalsIgnoreCase("admin") || status.equalsIgnoreCase("client"))) {
				status = null;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
	
		return status;
	}

	@Override
	public List<Client> getAccounts() {
		List<Client> accounts = new ArrayList<>();
		
		Connection connection = null;
		
		try {
			// Establish connection with database
			connection = getConnection();
			
			// Query to get all the clients
			String sql = "SELECT * FROM client";
			
			// Create prepared statement
			PreparedStatement ps = connection.prepareStatement(sql);
			
			ResultSet resultSet = ps.executeQuery();		// Run query and store results of all clients
			
			while (resultSet.next()) {
				Client client = new Client();
				Billing billingInfo = new Billing();
				Shipping shippingInfo = new Shipping();
				
				// Get client account information
				client.setID(resultSet.getLong("ID"));
				client.setStatus(resultSet.getString("status"));
				client.setUsername(resultSet.getString("username"));
				client.setPassword(resultSet.getString("password"));
				client.setFirstName(resultSet.getString("firstName"));
				client.setLastName(resultSet.getString("lastName"));
				client.setEmail(resultSet.getString("email"));
				
				// Get client shipping information
				shippingInfo.setAddressS(resultSet.getString("addressS"));
				shippingInfo.setCountryS(resultSet.getString("countryS"));
				shippingInfo.setPhoneS(resultSet.getLong("phoneS"));
				shippingInfo.setProvinceS(resultSet.getString("provinceS"));
				shippingInfo.setZipS(resultSet.getString("zipS"));
				
				client.setShipping(shippingInfo);		// Set shipping information for the client
				
				// Get client billing information
				billingInfo.setAddressB(resultSet.getString("addressB"));
				billingInfo.setCc(resultSet.getLong("cc"));
				billingInfo.setCcExpiryMon(resultSet.getInt("ccExpiryMon"));
				billingInfo.setCcExpiryYr(resultSet.getInt("ccExpiryYr"));
				billingInfo.setCountryB(resultSet.getString("countryB"));
				billingInfo.setCvv(resultSet.getInt("cvv"));
				billingInfo.setPhoneB(resultSet.getLong("phoneB"));
				billingInfo.setProvinceB(resultSet.getString("provinceB"));
				billingInfo.setZipB(resultSet.getString("zipB"));
				
				client.setBilling(billingInfo);		// Set billing information for the client
				
				accounts.add(client);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			closeConnection(connection);
		}
		
		return accounts;
	}
}
