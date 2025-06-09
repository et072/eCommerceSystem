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

import dao.OrdersDAO;
import dao.OrdersDAOImpl;
import model.Cart;
import model.Item;

/**
 * Servlet implementation class OrderServlet
 * Handles the requests to process of orders and updating of database to reflect it.
 * Web app does not use a real payment system. Handles logic that simulates credit card authorization failure.
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrdersDAO ordersDAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OrderServlet() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
    	ServletContext context = config.getServletContext();
    	
    	// Set path to order database file
		String path = context.getRealPath("/dbFile/order.db");
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
		// Get current session
		HttpSession session = request.getSession(true);
		
		Cart cart;
		
		synchronized (session) { // synchronized to prevent concurrent updates
			// Retrieve the shopping cart for this session, if any. Otherwise, create one.
			cart = (Cart) session.getAttribute("cart");
			
			if (cart == null) { // No cart, create one.
				cart = new Cart();
				session.setAttribute("cart", cart); // Save it into session
			}
		}
		
		/*
		 * Web application does not run through an actual payment system
		 * Used a counter to simulate credit card authorization failing. Fails every third attempt.
		 * Set session attribute that counts the number of credit card authorization attempts to do this.
		 */
		int counter = Integer.parseInt(String.valueOf(session.getAttribute("CCCounter")));
		session.setAttribute("CCCounter", counter + 1);
		
		// Create order and update database
		ordersDAO.createOrder(cart, String.valueOf(session.getAttribute("userid")));
		
		// Get total price of the cart and set it as an attribute to pass along
		double totalPrice = cart.getTotalPrice();
		request.setAttribute("totalPrice", totalPrice);
		
		// Order complete. Clear the cart and pass along the summary of items as an attribute for the order completion screen.
		List<Item> summary = cart.getItems();
		session.setAttribute("summary", summary);
		cart.clear();
		
		RequestDispatcher req = request.getRequestDispatcher("jsp/order.jsp");
		req.forward(request, response);
	}

}
