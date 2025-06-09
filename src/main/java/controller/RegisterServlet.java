package controller;

import java.io.IOException;

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
import model.Client;

/**
 * Servlet implementation class RegisterServlet
 * Handles user registration requests
 * 
 * Possible improvements:
 * Change the setting of user session attributes to simply passing a Client object/bean as an attribute
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ClientsDAO clientsDAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegisterServlet() {
        super();
    }

	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		
		// Set path to client database file
		String path = context.getRealPath("/dbFile/client.db");
		clientsDAO= new ClientsDAOImpl();

		clientsDAO.setPath(path);
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
		String url = "";
		String base = "jsp/";
		
		// Get current session
		HttpSession session = request.getSession(true);
		
		try {
			// Try registering the client using user provided input, found in the request
			Client client = clientsDAO.register(request);
			
			// If the user has been registered successfully
			if (client != null) {
				setSessionScope(request, response, client);
				
				// Checks if the user is in the process of checking out.
				String redirect = String.valueOf(session.getAttribute("redirect"));
				
				// User is trying to checkout. Redirect them to checkout page.
				if (redirect.equalsIgnoreCase("redirect")) {
					url = base + "checkOut.jsp";
					session.setAttribute("redirect", "");
				}
				
				// User is not trying to checkout. Redirect them to account page.
				else {
					url = base + "account.jsp";
				}
				
				session.setAttribute("loggedIn", "yes");
				session.setAttribute("status", "client"); 
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
		RequestDispatcher req = request.getRequestDispatcher(url);
		req.forward(request, response);
	}

	private void setSessionScope(HttpServletRequest request, HttpServletResponse response, Client client) {
		// Get current session
		HttpSession session = request.getSession(true);
		
		// Set session attributes related to the user
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
