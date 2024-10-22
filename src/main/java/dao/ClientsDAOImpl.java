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
	public Client login(String username, String password) {

		Client client = null;

		String sql = "select * from client where username = ? and password = ?";
		Connection connection = null;
		try {
			// Establish connection
			connection = getConnection();
			
			// Prepare statement
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, username);
			statement.setString(2, password);
			
			ResultSet resultSet =  statement.executeQuery();

			if (resultSet.next()) {

				client = new Client();

				// Set up client
				client.setID(resultSet.getLong("ID"));
				client.setStatus(resultSet.getString("status"));
				client.setUsername(resultSet.getString("username"));
				client.setPassword(resultSet.getString("password"));
				client.setFirstName(resultSet.getString("firstName"));
				client.setLastName(resultSet.getString("lastName"));
				client.setEmail(resultSet.getString("email"));

				// Set up Billing
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

				client.setBilling(billing);

				// Set up address
				Shipping shipping = new Shipping();

				shipping.setAddressS(resultSet.getString("addressS"));
				shipping.setProvinceS(resultSet.getString("provinceS"));
				shipping.setCountryS(resultSet.getString("countryS"));
				shipping.setZipS(resultSet.getString("zipS"));
				shipping.setPhoneS(resultSet.getLong("phoneS"));

				client.setShipping(shipping);
				
				System.out.println("check login");
				
			} 
			else {		// Login failed 
				return null; 
			}

		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return client;
	}

	@Override
	public Client register(HttpServletRequest request) {

		Client client = null;

		// Create client instance
		String sq = "insert into client (status, username, password, firstName, lastName, email, addressS, provinceS, countryS, zipS, phoneS, cc, ccExpiryMon, ccExpiryYr, cvv, addressB, provinceB, countryB, zipB, phoneB)" +
				"values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Connection connection = null;

		System.out.println("register was called");

		try {

			// Client info
			String status = "client";
			String username = request.getParameter("usernameR");
			String password = request.getParameter("passwordR");
			String firstName = request.getParameter("firstNameR");
			String lastName = request.getParameter("lastNameR");
			String email = request.getParameter("emailR");

			// Shipping info
			String addressS = request.getParameter("addressShippingR");
			String provinceS = request.getParameter("provinceShippingR");
			String countryS = request.getParameter("countryShippingR");
			String zipS = request.getParameter("zipCodeShippingR");
			int phoneS = Integer.parseInt(request.getParameter("phoneShippingR").replaceAll("[()\\s-]+", "")); // This will require some special formating

			// Billing info
			long cc = Long.parseLong(request.getParameter("creditCardR").replaceAll("[^0-9]", "")); // Will require removal of white space and conversion into 
			int ccExpiryMon = Integer.parseInt(request.getParameter("ccExpiryMonR").replaceAll("[^0-9]", "")); // will need to run a check to make sure the value is between 1 - 12
			int ccExpiryYr = Integer.parseInt(request.getParameter("ccExpiryYrR").replaceAll("[^0-9]", "")); // will need to run a check to make sure the value is between 1 - 12
			int cvv = Integer.parseInt(request.getParameter("cvvR").replaceAll("[^0-9]", "")); // must have 3-4 digits in length
			String addressB = request.getParameter("addressBillingR");
			String provinceB = request.getParameter("provinceBillingR");
			String countryB = request.getParameter("countryBillingR");
			String zipB = request.getParameter("zipCodeBillingR");
			int phoneB = Integer.parseInt(request.getParameter("phoneBillingR").replaceAll("[()\\s-]+", "")); // This will require some special formating; // this will require a lot of formating 

			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sq);

			statement.setString(1, status);
			statement.setString(2, username);
			statement.setString(3, password);
			statement.setString(4, firstName);
			statement.setString(5, lastName);
			statement.setString(6, email);
			statement.setString(7, addressS);
			statement.setString(8, provinceS);
			statement.setString(9, countryS);
			statement.setString(10, zipS);
			statement.setInt(11, phoneS);
			statement.setLong(12, cc);
			statement.setInt(13, ccExpiryMon);
			statement.setInt(14, ccExpiryYr);
			statement.setInt(15, cvv);
			statement.setString(16, addressB);
			statement.setString(17, provinceB);
			statement.setString(18, countryB);
			statement.setString(19, zipB);
			statement.setInt(20, phoneB);

			statement.executeUpdate();

			client = login(username, password);
		} 
		catch (NumberFormatException e) {
			System.out.println("number format exception occurred: " + e);
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}

		return client;
	}

	@Override
	public Client updateAccountInfo(String id, String username, String password, String firstName, String lastName, String email) {
		Client client = null;

		String sql = "update client set username = ?, password = ?, firstName = ?, lastName = ?, email = ? where id = ?";
		Connection connection = null;
		try {
			// Establish connection
			connection = getConnection();
			
			// Prepare statement
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, username);
			statement.setString(2, password);
			statement.setString(3, firstName);
			statement.setString(4, lastName);
			statement.setString(5, email);
			statement.setInt(6, Integer.parseInt(id));

			statement.executeUpdate();
			
			client = login(username, password);
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return client;
	}

	@Override
	public Client updateAccountShipping(String id, String strAddr, String province, String country, String zip, String phoneNum) {

		Client client = null;

		String sql = "update client set addressS = ?, provinceS = ?, countryS = ?, zipS = ?, phoneS = ? where id = ?";
		Connection connection = null;
		try {
			// Establish connection
			connection = getConnection();
			
			// Prepare statement
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, strAddr);
			statement.setString(2, province);
			statement.setString(3, country);
			statement.setString(4, zip);
			statement.setLong(5, Long.parseLong(phoneNum.replaceAll("\\D+", "")));
			statement.setInt(6, Integer.parseInt(id));

			statement.executeUpdate();

			client = login(id);
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return client;
	}

	@Override
	public Client updateAccountCC(String id, String cc, String expMon, String expYr, String cvv) {
		
		System.out.println(id);
		Client client = null;

		String sql = "update client set cc = ?, ccExpiryMon = ?, ccExpiryYr = ?, cvv = ? where id = ?";
		Connection connection = null;
		try {
			// Establish connection
			connection = getConnection();
			
			// Prepare statement
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setLong(1, Long.parseLong(cc));
			statement.setInt(2, Integer.parseInt(expMon));
			statement.setInt(3, Integer.parseInt(expYr));
			statement.setInt(4, Integer.parseInt(cvv));
			statement.setInt(5, Integer.parseInt(id));

			statement.executeUpdate();
			
			
			
			client = login(id);
		} 
		catch(NumberFormatException e) {

		}
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return client;
	}

	@Override
	public Client updateAccountBilling(String id, String address, String province, String country, String zip, String phone) {
		Client client = null;

		String sql = "update client set addressB = ?, provinceB = ?, countryB = ?, zipB = ?, phoneB = ? where id = ?";
		Connection connection = null;
		try {
			// Establish connection
			connection = getConnection();
			
			// Prepare statement
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setString(1, address);
			statement.setString(2, province);
			statement.setString(3, country);
			statement.setString(4, zip);
			statement.setLong(5, Long.parseLong(phone.replaceAll("\\D+", "")));
			statement.setInt(6, Integer.parseInt(id));

			statement.executeUpdate();

			client = login(id);
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return client;
	}

	public Client login(String id) {
		Client client = null;

		String sql = "select * from client where id = ?";
		Connection connection = null;
		try {
			// Establish connection
			connection = getConnection();
			
			// Prepare statement
			PreparedStatement statement = connection.prepareStatement(sql);

			statement.setInt(1, Integer.parseInt(id));

			ResultSet resultSet =  statement.executeQuery();

			if (resultSet.next()) {

				client = new Client();

				// Set up client
				client.setID(resultSet.getInt("ID"));
				client.setStatus(resultSet.getString("status"));
				client.setUsername(resultSet.getString("username"));
				client.setPassword(resultSet.getString("password"));
				client.setFirstName(resultSet.getString("firstName"));
				client.setLastName(resultSet.getString("lastName"));
				client.setEmail(resultSet.getString("email"));

				// Set up Billing
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

				client.setBilling(billing);

				// Set up address
				Shipping shipping = new Shipping();

				shipping.setAddressS(resultSet.getString("addressS"));
				shipping.setProvinceS(resultSet.getString("provinceS"));
				shipping.setCountryS(resultSet.getString("countryS"));
				shipping.setZipS(resultSet.getString("zipS"));
				shipping.setPhoneS(resultSet.getLong("phoneS"));

				client.setShipping(shipping);
				
				
				
			} 
			else {	// Login failed 
				return null; 
			}

		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return client;
	}

	@Override
	public String getStatus(String id) {
		String status = null;
		String sql = "select status from client where id = ?";
		Connection connection = null;
		try {
			connection = getConnection();
			
			PreparedStatement statement = connection.prepareStatement(sql);
			
			statement.setLong(1, Long.parseLong(id));
			
			ResultSet resultSet = statement.executeQuery();
			
			resultSet.next();
			
			status = resultSet.getString("status");
			
			if (!(status.equalsIgnoreCase("admin") || status.equalsIgnoreCase("client"))) {
				status = null;
			}
			
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
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
			// Establish connection
			connection = getConnection();
			
			// Query sent to the DB
			String query = "select * from client";
			
			// Prepared statement
			PreparedStatement statement = connection.prepareStatement(query);
			
			ResultSet resultSet = statement.executeQuery();
			
			while (resultSet.next()) {
				Client client = new Client();
				Billing billingInfo = new Billing();
				Shipping shippingInfo = new Shipping();
				
				// Get client account info
				client.setID(resultSet.getLong("ID"));
				client.setStatus(resultSet.getString("status"));
				client.setUsername(resultSet.getString("username"));
				client.setPassword(resultSet.getString("password"));
				client.setFirstName(resultSet.getString("firstName"));
				client.setLastName(resultSet.getString("lastName"));
				client.setEmail(resultSet.getString("email"));
				
				// Get client shipping info
				shippingInfo.setAddressS(resultSet.getString("addressS"));
				shippingInfo.setCountryS(resultSet.getString("countryS"));
				shippingInfo.setPhoneS(resultSet.getLong("phoneS"));
				shippingInfo.setProvinceS(resultSet.getString("provinceS"));
				shippingInfo.setZipS(resultSet.getString("zipS"));
				client.setShipping(shippingInfo);
				
				// Get client billing info
				billingInfo.setAddressB(resultSet.getString("addressB"));
				billingInfo.setCc(resultSet.getLong("cc"));
				billingInfo.setCcExpiryMon(resultSet.getInt("ccExpiryMon"));
				billingInfo.setCcExpiryYr(resultSet.getInt("ccExpiryYr"));
				billingInfo.setCountryB(resultSet.getString("countryB"));
				billingInfo.setCvv(resultSet.getInt("cvv"));
				billingInfo.setPhoneB(resultSet.getLong("phoneB"));
				billingInfo.setProvinceB(resultSet.getString("provinceB"));
				billingInfo.setZipB(resultSet.getString("zipB"));
				client.setBilling(billingInfo);
				
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
