package controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import model.Cart;

/**
 * Servlet implementation class HomeServlet
 * Handles setting session attributes for the initial landing page when the web application is started for the first time.
 */
@WebServlet("/home")
public class HomeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeServlet() {
        super();
    }
    
    public void init(ServletConfig config) throws ServletException {
    	
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
		
		// Path to home page
		String url = "jsp/home.jsp";
		
		// New cart
		Cart cart = new Cart();
		
		session.setAttribute("cart", cart);		// Save cart in current session
		
		// Set new session attributes
		session.setAttribute("CCCounter", 1);
		session.setAttribute("loggedIn", "no");
		session.setAttribute("status", "null");
		session.setAttribute("orders", null);
		session.setAttribute("cart", new Cart());
		session.setAttribute("ordersTotalPrice", 0);
		
		RequestDispatcher req = request.getRequestDispatcher(url);
		req.forward(request, response);
	}

}
