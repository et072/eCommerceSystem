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
		doPost(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		Cart cart;
		synchronized (session) { // Synchronized to prevent concurrent updates
			// Retrieve the shopping cart for this session, if any. Otherwise, create one.
			cart = (Cart) session.getAttribute("cart");
			if (cart == null) { // If no cart exists, create one.
				cart = new Cart();
				session.setAttribute("cart", cart); // Save cart into the session
			}
		}
				
		String base = "/jsp/";
		String action = request.getParameter("action");
		String url = base + "cart.jsp"; 
		String  itemID = request.getParameter("itemID");
		String qty = request.getParameter("qty");
		if (action != null) { 
			switch (action) {
			case "addItem":
				addItem(request, response,Long.parseLong(itemID),Integer.parseInt(qty));
				break;
			case "removeItem":
				removeItem(request, response,Long.parseLong(itemID));
				break;				
			case "updateItem":
				updateItem(request, response,Long.parseLong(itemID), Integer.parseInt(qty));
				break;
			}
		}
			RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
			requestDispatcher.forward(request, response);
	}
	
	private void addItem(HttpServletRequest request,
			HttpServletResponse response, long itemId, int qty) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");
			
			ItemsDAO i = new ItemsDAOImpl(); 
			i.setPath( (String) getServletContext().getAttribute("Items_path"));
			
			Item temp = i.getItem(itemId);
			temp.setQuantity(qty);
			cart.add(temp.getItemID(), temp.getCategoryID(), temp.getName(), temp.getDescription(), temp.getBrand(), temp.getQuantity(), temp.getPrice());
			
			session.setAttribute("cart", cart);
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private void removeItem(HttpServletRequest request,
			HttpServletResponse response, long itemId) throws ServletException, IOException {		
		try {
			HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");			
			cart.remove(itemId);
			session.setAttribute("cart", cart);
		} 
		catch (Exception e) {
			System.out.println(e);
		}	
		
	}
	
	private void updateItem(HttpServletRequest request,
			HttpServletResponse response, long itemId, int newQty) throws ServletException, IOException {		
		try {
			HttpSession session = request.getSession();
			Cart cart = (Cart) session.getAttribute("cart");			
			cart.update(itemId, newQty);;

			session.setAttribute("cart", cart);
		} 
		catch (Exception e) {
			System.out.println(e);
		}	
		
	}
	

}
