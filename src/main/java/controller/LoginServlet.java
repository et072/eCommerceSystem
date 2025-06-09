package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ClientsDAO;
import dao.ClientsDAOImpl;
import dao.ItemsDAO;
import dao.ItemsDAOImpl;
import dao.OrdersDAO;
import dao.OrdersDAOImpl;
import model.Cart;
import model.Client;
import model.Order;

/**
 * Servlet implementation class LoginServlet
 * Handles login and sign out requests.
 * 
 * Possible improvements:
 * Create separate setSessionScope methods for client and admin, and move all the setting of session
 * attributes related into them, rather than containing them in the doPost method.
 * 
 * Condense the setting of attributes by simply passing a Client object/bean as an attribute
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ClientsDAO clientsDAO;
    private OrdersDAO ordersDAO;
    private ItemsDAO itemsDAO;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
    }
    
    // Initialize servlet and set paths to database files
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = this.getServletContext();
		
		// Set path to client database file
		String path = context.getRealPath("/dbFile/client.db");
		clientsDAO = new ClientsDAOImpl();
		clientsDAO.setPath(path);
		
		// Set path to items database file
		path = context.getRealPath("/dbFile/items.db");
		itemsDAO = new ItemsDAOImpl();
		itemsDAO.setPath(path);
		
		// Set path to orders database file
		path = context.getRealPath("/dbFile/order.db");
		ordersDAO = new OrdersDAOImpl();;
		ordersDAO.setPath(path);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getParameter("action");		// Get action
		
		String base = "jsp";
		String url = "";
		
		HttpSession session = request.getSession(true);
		RequestDispatcher req;
		
		switch(action) {
			// User is trying to log in
			case "login":
				// Get user input for username and password
				String username = request.getParameter("usernameL");
				String password = request.getParameter("passwordL");
				
				Client client = clientsDAO.login(username, password);		// Log in using provided credentials
				
				// If the client exists 
				if (client != null) {
					// User is a customer
					if (client.getStatus().equalsIgnoreCase("client")) {
						// Check if the user is in the process of checking out.
						String redirect = String.valueOf(request.getAttribute("redirect"));
						
						// User is trying to checkout. Redirect them to checkout page.
						if (redirect.equalsIgnoreCase("redirect")) {
							url = base + "/checkOut.jsp";
						}
						
						// User is logging in normally. Redirect them to account page.
						else {
							url = base + "/account.jsp";							
						}
						
						// Set session attributes related to the customer.
						setSessionScopes(request, response, client);
						session.setAttribute("loggedIn", "yes");
						session.setAttribute("status", "client");
						
						// Get all the orders for the logged in user
						List<Order> orders = ordersDAO.getOrders(String.valueOf(client.getID()), clientsDAO, itemsDAO);
						
						double ordersTotalPrice = 0.0;		// Stores total spent on orders by logged in user
						
						// Calculate the total spent on orders by the logged in customer
						for (Order order: orders) {
							ordersTotalPrice += order.getTotalPrice();
						}
						
						// Set session attribute for the customer's order history
						session.setAttribute("ordersTotalPrice", ordersTotalPrice);
						session.setAttribute("orders", orders);
					}
					
					// User is an admin
					else if(client.getStatus().equalsIgnoreCase("admin")) {
						url = "AdminServlet";		// Redirect to the AdminServlet
						
						// Set session attributes related to the admin.
						setSessionScopes(request, response, client);
						session.setAttribute("loggedIn", "yes");
						session.setAttribute("status", "admin");
					}
					
					// Error logging in. Return user back to login page and set error message.
					else {
						url = base + "/login.jsp";
						request.setAttribute("error", "error");
					}
					
				}
				
				// Error logging in. Return user back to login page and set error message.
				else {
					request.setAttribute("error", "error");
					url = base + "/login.jsp";
				}
				
				break;
			
			// User is trying to sign out
			case "signout":
				url = base + "/home.jsp";		// Redirect back to the home page after signing out
				
				// Reset session attributes related to login status, user permission level, order history, and cart.
				session.setAttribute("loggedIn", "no");
				session.setAttribute("status", "null");
				session.setAttribute("orders", null);
				session.setAttribute("cart", new Cart());
				
				break;
				
			// User is refreshing the page
			case "refresh":
				url = base + "/account.jsp";		// Redirect back to the account page
				
				String clientID = String.valueOf(session.getAttribute("userid"));		// ID of logged in user
				
				// Get logged in user's order history
				List<Order> orders = ordersDAO.getOrders(clientID, clientsDAO, itemsDAO);
				
				double ordersTotalPrice = 0.0;		// Stores total spent on orders by logged in user
				
				// Calculate the total spent on orders by the logged in customer
				for (Order order: orders) {
					ordersTotalPrice += order.getTotalPrice();
				}
				
				// Set session attribute for the customer's order history
				session.setAttribute("ordersTotalPrice", ordersTotalPrice);
				session.setAttribute("orders", orders);
				
				break;
		}
		
		req = request.getRequestDispatcher(url);
		req.forward(request, response);
	}
	
	private void setSessionScopes(HttpServletRequest request, HttpServletResponse response, Client client) {
		// Get current session
		HttpSession session = request.getSession(true);
		
		// Mask sensitive user information and set the masked information as an attribute
		session.setAttribute("password", maskPassword(client.getPassword()));
		session.setAttribute("creditCardNum", maskCC(client.getBilling().getCc()));
		session.setAttribute("cvv", maskCVV(client.getBilling().getCvv()));
		
		// Set session attributes for the user
		session.setAttribute("userid", client.getID());
		session.setAttribute("username", client.getUsername());
		session.setAttribute("firstName", client.getFirstName());
		session.setAttribute("lastName", client.getLastName());
		session.setAttribute("email", client.getEmail());
		session.setAttribute("shippingAddress", client.getShipping().getAddressS());
		session.setAttribute("shippingProvince", client.getShipping().getProvinceS());
		session.setAttribute("shippingCountry", client.getShipping().getCountryS());
		session.setAttribute("shippingZip", client.getShipping().getZipS());
		session.setAttribute("shippingPhone", client.getShipping().getPhoneS());
		session.setAttribute("ccExpiryMon", client.getBilling().getCcExpiryMon());
		session.setAttribute("ccExpiryYr", client.getBilling().getCcExpiryYr());
		session.setAttribute("billingAddress", client.getBilling().getAddressB());
		session.setAttribute("billingProvince", client.getBilling().getProvinceB());
		session.setAttribute("billingCountry", client.getBilling().getCountryB());
		session.setAttribute("billingZip", client.getBilling().getZipB());
		session.setAttribute("billingPhone", client.getBilling().getPhoneB());
	}
	
	// Masks password
	private String maskPassword(String password) {
		password = "*".repeat(password.length());
		
		return password;
	}
	
	// Masks all but the last 4 digits of the credit card number
	private String maskCC(Long cc) {
		String strCC = cc.toString();
		StringBuffer stringCC = new StringBuffer(strCC);
		
		for (int i = 0; i < strCC.length()-4; i++) {
			stringCC.setCharAt(i, '*');
		}
		
		return stringCC.toString();
	}
	
	// Masks the CVV
	private String maskCVV(int cvv) {
		String strCVV = String.valueOf(cvv);
		
		strCVV = "*".repeat(strCVV.length());
		
		return strCVV;
	}
}
