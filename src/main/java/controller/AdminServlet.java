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
import model.Client;
import model.Item;
import model.Order;

/**
 * Servlet implementation class AdminServlet
 * Handles all admin related operations, such as updating customer account information, viewing all order history, and viewing
 * all website products.
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ItemsDAO itemsDAO;
	ClientsDAO clientsDAO;
	OrdersDAO ordersDAO;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminServlet() {
        super();
    }
    
    // Initialize servlet and set the path to all database files
    public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = this.getServletContext();
		String path = context.getRealPath("/dbFile/client.db");
		clientsDAO = new ClientsDAOImpl();
		clientsDAO.setPath(path);

		path = context.getRealPath("/dbFile/items.db");
		itemsDAO = new ItemsDAOImpl();
		itemsDAO.setPath(path);

		path = context.getRealPath("/dbFile/order.db");
		ordersDAO = new OrdersDAOImpl();
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
		// Get action that is being taken (i.e. updating shipping, billing, or account info)
		String action = request.getParameter("action");
		
		// Path to admin.jsp
		String url = "";
		String base = "jsp/";
		url = base + "admin.jsp";
		
		// Get current session
		HttpSession session = request.getSession(true);
		
		String clientID = request.getParameter("clientIdA");	// Client ID of the account being updated
		
		// If an action is being taken
		if (action != null) {
			// Action determines what will be done
			switch (action) {
				// Update account information
				case "updateAccountInfo":
					// Path to next web page
					url = base + "admin.jsp";

					// Get updated general account information parameters
					String username = request.getParameter("aiu" + clientID);
					String password = request.getParameter("aip" + clientID);
					String firstName = request.getParameter("aif" + clientID);
					String lastName = request.getParameter("ail" + clientID);
					String email = request.getParameter("aie" + clientID);
					
					// Update the general account information
					clientsDAO.updateAccountInfo(clientID, username, password, firstName, lastName, email);
	
					break;
					
				// Update shipping information
				case "updateAccountShipping":
					// Path to next web page
					url = base + "admin.jsp";
					
					// Get updated shipping information parameters				
					String shippingAddress = request.getParameter("sa" + clientID);
					String shippingProvince = request.getParameter("spr" + clientID);
					String shippingCountry = request.getParameter("sc" + clientID);
					String shippingZip = request.getParameter("sz" + clientID);
					String shippingPhone = request.getParameter("sph" + clientID);
					
					// Update account shipping information
					clientsDAO.updateAccountShipping(clientID, shippingAddress, shippingProvince, shippingCountry, shippingZip, shippingPhone);
	
					break;
					
				// Update payment information
				case "updateAccountCC":
					// Path to next web page
					url = base + "admin.jsp";
					
					// Get updated credit card information parameters
					String cc = request.getParameter("cc" + clientID);
					String ccExpiryMon = request.getParameter("ccem" + clientID);
					String ccExpiryYr = request.getParameter("ccey" + clientID);
					String cvv = request.getParameter("ccv" + clientID);
					
					// Update account payment information
					clientsDAO.updateAccountCC(clientID, cc, ccExpiryMon, ccExpiryYr, cvv);
	
					break;
					
				// Update billing information
				case "updateAccountBilling":
					// Path to next web page
					url = base + "admin.jsp";
					
					// Get updated billing information parameters
					String billingAddress = request.getParameter("ba" + clientID);
					String billingProvince = request.getParameter("bpr" + clientID);
					String billingCountry = request.getParameter("bc" + clientID);
					String billingZip = request.getParameter("bz" + clientID);
					String billingPhone = request.getParameter("bph" + clientID);
					
					// Update account billing information for the user
					clientsDAO.updateAccountBilling(clientID, billingAddress, billingProvince, billingCountry, billingZip, billingPhone);
					
					break;
					
				// Visit admin accounts page
				case "account":
					url = base + "admin.jsp";
					break;
					
				// Get and view all order records (sales history)
				case "order":
					// Path to next web page
					url = base + "orderPreview.jsp";
					
					// Get all order record and pass them along as an attribute
					List<Order> orders = ordersDAO.getOrders(String.valueOf(session.getAttribute("userid")), clientsDAO, itemsDAO);
					request.setAttribute("orders", orders);
					
					double ordersTotalPrice = 0.0;			// Total cost of all orders combined
					
					// Calculate the sum of all order costs
					for (Order order: orders) {
						ordersTotalPrice += order.getTotalPrice();
					}
					
					// Pass the total cost of all orders as an attribute
					request.setAttribute("ordersTotalPrice", ordersTotalPrice);
					
					break;
					
				// Get and view list of all products
				case "item":
					// Path to next web page
					url = base + "adminList.jsp";
					
					// Get all products and their associated information
					List<Item> allItems = itemsDAO.findAllItems();
					request.setAttribute("itemList", allItems);		// Pass information along as attribute
					
					break;
			}
		}
		
		// View all user accounts
		List<Client> allAccounts = clientsDAO.getAccounts();
		request.setAttribute("accountList", allAccounts);
		
		RequestDispatcher req = request.getRequestDispatcher(url);
		req.forward(request, response);
	}

}
