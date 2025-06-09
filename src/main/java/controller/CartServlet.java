package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import dao.ItemsDAO;
import dao.ItemsDAOImpl;
import model.Cart;
import model.Item;

/**
 * Servlet implementation class CartServlet
 * Handles all shopping cart related operations, such as adding, removing, or updating an item into the cart.
 */
@WebServlet("/CartServlet")
public class CartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CartServlet() {
        super();
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
		// Get current session and declare a new cart object
		HttpSession session = request.getSession(true);
		Cart cart;
		
		// Synchronized to prevent concurrent updates
		synchronized (session) {
			// Retrieve the shopping cart for this session, if any. Otherwise, create one.
			cart = (Cart) session.getAttribute("cart");
			// If no cart, create one.
			if (cart == null) {
				cart = new Cart();
				session.setAttribute("cart", cart); // Save cart into current session
			}
		}
		
		// Path to shopping cart page
		String base = "/jsp/";
		String url = base + "cart.jsp";
		
		// Get action being taken, quantity of products, and item ID
		String action = request.getParameter("action");
		String itemID = request.getParameter("itemID");
		String qty = request.getParameter("qty");
		
		// If an action is being taken
		if (action != null) { 
			// Determine what to do based on action being taken
			switch (action) {
				// Add an item to the cart
				case "addItem":
					addItem(request, response, Long.parseLong(itemID), Integer.parseInt(qty));
					
					break;
				
				// Remove an item from the cart
				case "removeItem":
					removeItem(request, response, Long.parseLong(itemID));
					
					break;				
				
				// Update an item in the cart
				case "updateItem":
					updateItem(request, response, Long.parseLong(itemID), Integer.parseInt(qty));
					
					break;
			}
		}
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
		requestDispatcher.forward(request, response);
	}
	
	private void addItem(HttpServletRequest request, HttpServletResponse response, long itemID, int qty) throws ServletException, IOException {
		try {
			// Get current session and get current cart
			HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");
			
			// Create new item DAO to get item ID and path to items database
			ItemsDAO items = new ItemsDAOImpl();
			items.setPath((String) getServletContext().getAttribute("Items_path"));
			
			// Get the current item being added based on the provided item ID
			Item temp = items.getItem(itemID);
			temp.setQuantity(qty);
			
			// Add new item to the cart
			cart.add(temp.getItemID(), temp.getCategoryID(), temp.getName(), temp.getDescription(), temp.getBrand(), temp.getQuantity(), temp.getPrice());
			
			// Update cart of current session
			session.setAttribute("cart", cart);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void removeItem(HttpServletRequest request, HttpServletResponse response, long itemID) throws ServletException, IOException {
		try {
			// Get current session and get current cart
			HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");
			
			cart.remove(itemID);		// Remove item from the cart, provided an item ID
			
			// Update cart of current session
			session.setAttribute("cart", cart);
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	private void updateItem(HttpServletRequest request, HttpServletResponse response, long itemID, int newQty) throws ServletException, IOException {
		try {
			// Get current session and get current cart
			HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");
			
			cart.update(itemID, newQty);		// Update quantity of item in the cart, provided an item ID
			
			// Update cart of current session
			session.setAttribute("cart", cart);
		}
		catch (Exception e) {
			e.printStackTrace();
		}	
	}
}
