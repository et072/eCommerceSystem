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

import dao.OrdersDAO;
import dao.OrdersDAOImpl;
import model.Cart;
import model.Item;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private OrdersDAO ordersDao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OrderServlet() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = config.getServletContext();;
		String path = context.getRealPath("/dbFile/order.db");
		ordersDao = new OrdersDAOImpl();
		
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


		// Increment CCCounter
		int counter = Integer.parseInt(String.valueOf(session.getAttribute("CCCounter")));
		session.setAttribute("CCCounter", counter+1);

		// Create order update
		ordersDao.createOrder(cart, String.valueOf(session.getAttribute("userid")));

		double totalPrice = cart.getTotalPrice();

		request.setAttribute("totalPrice", totalPrice);

		// Clear cart
		List<Item> summary = cart.getItems();
		session.setAttribute("summary", summary);
		cart.clear();

		RequestDispatcher req = request.getRequestDispatcher("jsp/order.jsp");
		req.forward(request, response);
	}
}
