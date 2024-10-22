package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
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
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private ClientsDAO clientsDao;
    private OrdersDAO ordersDao;
    private ItemsDAO itemsDao;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginServlet() {
        super();
        
    }

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = this.getServletContext();
		String path = context.getRealPath("/dbFile/client.db");
		clientsDao = new ClientsDAOImpl();
		clientsDao.setPath(path);
		
		path = context.getRealPath("/dbFile/items.db");
		itemsDao = new ItemsDAOImpl();
		itemsDao.setPath(path);
		
		path = context.getRealPath("/dbFile/order.db");
		ordersDao = new OrdersDAOImpl();;
		ordersDao.setPath(path);
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
		
		String action = request.getParameter("action");
		String base = "jsp";
		String url = "";
		
		HttpSession session = request.getSession(true);
		RequestDispatcher req;
		switch(action) {
			case "login":
				String username = request.getParameter("usernameL");
				String password = request.getParameter("passwordL");
				
				Client client = clientsDao.login(username, password);
				
				if(client != null) {
					if(client.getStatus().equalsIgnoreCase("client")) {
						String redirect = String.valueOf(request.getAttribute("redirect"));
						
						if(redirect.equalsIgnoreCase("redirect")) {
							url = base + "/checkOut.jsp";
						} 
						else {
							url = base + "/account.jsp";							
						}
						setSessionScopes(request, response, client);
						session.setAttribute("loggedIn", "yes");
						session.setAttribute("status", "client");
						List<Order> orders = ordersDao.getOrders(String.valueOf(client.getID()),clientsDao, itemsDao);
						double ordersTotalPrice = 0.0;
						for(Order order: orders) {
							ordersTotalPrice += order.getTotalPrice();
						}
						session.setAttribute("ordersTotalPrice", ordersTotalPrice);
						session.setAttribute("orders", orders);
					} 
					else if(client.getStatus().equalsIgnoreCase("admin")) {
						url = "AdminServlet";
						setSessionScopes(request, response, client);
						session.setAttribute("loggedIn", "yes");
						session.setAttribute("status", "admin");
					} 
					else {
						url = base + "/login.jsp";
						request.setAttribute("error", "error");
					}
					
				} 
				else {
					request.setAttribute("error", "error");
					url = base + "/login.jsp";
				}
				break;
			case "signout":
				url = base + "/home.jsp";
				session.setAttribute("loggedIn", "no");
				session.setAttribute("status", "null");
				session.setAttribute("orders", null);
				session.setAttribute("cart", new Cart());
				break;
			case "refresh":
				url = base + "/account.jsp";
				String clientID = String.valueOf(session.getAttribute("userid"));
				List<Order> orders = ordersDao.getOrders(clientID, clientsDao, itemsDao);
				double ordersTotalPrice = 0.0;
				for(Order order: orders) {
					ordersTotalPrice += order.getTotalPrice();
				}
				session.setAttribute("ordersTotalPrice", ordersTotalPrice);
				session.setAttribute("orders", orders);
				break;
		}
		
		req = request.getRequestDispatcher(url);
		req.forward(request, response);
	}
	
	private void setSessionScopes(HttpServletRequest request, HttpServletResponse response, Client client) {
		HttpSession session = request.getSession(true);
		
		// Setting session scope attributes
		// Can be condensed with session.setAttributes("client", client);
		session.setAttribute("userid", client.getID());
		session.setAttribute("username", client.getUsername());
		session.setAttribute("password", client.getPassword());
		session.setAttribute("firstName", client.getFirstName());
		session.setAttribute("lastName", client.getLastName());
		session.setAttribute("email", client.getEmail());
		session.setAttribute("shippingAddress", client.getShipping().getAddressS());
		session.setAttribute("shippingProvince", client.getShipping().getProvinceS());
		session.setAttribute("shippingCountry", client.getShipping().getCountryS());
		session.setAttribute("shippingZip", client.getShipping().getZipS());
		session.setAttribute("shippingPhone", client.getShipping().getPhoneS());
		session.setAttribute("creditCardNum", client.getBilling().getCc());
		session.setAttribute("ccExpiryMon", client.getBilling().getCcExpiryMon());
		session.setAttribute("ccExpiryYr", client.getBilling().getCcExpiryYr());
		session.setAttribute("cvv", client.getBilling().getCvv());
		session.setAttribute("billingAddress", client.getBilling().getAddressB());
		session.setAttribute("billingProvince", client.getBilling().getProvinceB());
		session.setAttribute("billingCountry", client.getBilling().getCountryB());
		session.setAttribute("billingZip", client.getBilling().getZipB());
		session.setAttribute("billingPhone", client.getBilling().getPhoneB());
	}

}
