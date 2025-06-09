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
 * Servlet implementation class UpdateAccountServlet
 * Handles the updating of existing account information.
 * 
 * Possible improvements:
 * setSessionScope can be simplified to simply setting a Client object/Bean attribute, rather than setting all its
 * components individually.
 */
@WebServlet("/UpdateAccountServlet")
public class UpdateAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ClientsDAO clientsDAO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateAccountServlet() {
        super();
    }
    
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		
		// Set path to client database file
		String path = context.getRealPath("/dbFile/client.db");
		clientsDAO = new ClientsDAOImpl();

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
		// Get user action
		String action = request.getParameter("action");
		
		String url = "";
		String base = "jsp/";
		
		Client client = null;
		
		// If there was an action taken
		if (action != null) {
			switch (action) {
				// Update general user account information
				case "updateAccountInfo":
					// Redirect user to account page and update their general account information.
					url = base + "account.jsp";
					client = updateAccountInfo(request, response);
					
					break;
					
				// Update user shipping information
				case "updateAccountShipping":
					// Redirect user to account page and update their shipping information.
					url = base + "account.jsp";
					client = updateAccountShipping(request, response);
					
					break;
					
				// Update user payment information
				case "updateAccountCC":
					// Redirect user to account page and update their payment information.
					url = base + "account.jsp";
					client = updateAccountCC(request, response);
					
					break;
					
				// Update user billing information
				case "updateAccountBilling":
					// Redirect user to account page and update their billing information.
					url = base + "account.jsp";
					client = updateAccountBilling(request, response);
					
					break;
			}
		}
		
		// Update failed, go back to account page with an error.
		if (client == null) {
			request.setAttribute("error", "error");
			url = base + "account.jsp";
		}
		
		// No error. Update successful.
		else {
			// Re-set session attributes for user with updated information.
			setSessionScope(request, response, client);
		}
		
		RequestDispatcher req = request.getRequestDispatcher(url);
		req.forward(request, response);
	}
	
	/*
	 * Sets attributes related to the user for their entire session.
	 * These attributes are used throughout the web application (i.e. checkout page)
	 */
	private void setSessionScope(HttpServletRequest request, HttpServletResponse response, Client client) {
		// Get current session
		HttpSession session = request.getSession(true);
		
		// Set session attributes related to the user.
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
	
	/*
	 * Takes user input and tries updating their account's general user information
	 * Returns a client object, which if null, indicates an error with the update.
	 */
	private Client updateAccountInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get current session
		HttpSession session = request.getSession(true);
		
		// Get user input related to updated general account information
		String username = request.getParameter("usernameU");
		String password = request.getParameter("passwordU");
		String firstName = request.getParameter("firstNameU");
		String lastName = request.getParameter("lastNameU");
		String email = request.getParameter("emailU");
		
		// Try updating general account information
		Client client = clientsDAO.updateAccountInfo(String.valueOf(session.getAttribute("userid")), username, password, firstName, lastName, email);
		
		return client;
	}
	
	/*
	 * Takes user input and tries updating their account's shipping information
	 * Returns a client object, which if null, indicates an error with the update.
	 */
	private Client updateAccountShipping(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get current session
		HttpSession session = request.getSession(true);
		
		// Get user input related to updated shipping information
		String address = request.getParameter("addressShippingU");
		String province = request.getParameter("provinceShippingU");
		String country = request.getParameter("countryShippingU");
		String zip = request.getParameter("zipCodeShippingU");
		String phone = request.getParameter("phoneShippingU");

		// Try updating shipping information
		Client client = clientsDAO.updateAccountShipping(String.valueOf(session.getAttribute("userid")), address, province, country, zip, phone);
		
		return client;
	}
	
	
	/*
	 * Takes user input and tries updating their account's payment information
	 * Returns a client object, which if null, indicates an error with the update.
	 */
	private Client updateAccountCC(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// Get current session
		HttpSession session = request.getSession(true);
		
		// Get user input related to updated payment information
		String cc = request.getParameter("creditCardU");
		String expMon = request.getParameter("ccExpiryMonU");
		String expYr = request.getParameter("ccExpiryYrU");
		String cvv = request.getParameter("cvvU");

		// Try updating payment information
		Client client = clientsDAO.updateAccountCC(String.valueOf(session.getAttribute("userid")), cc, expMon, expYr, cvv);
		
		return client;
	}
	
	/*
	 * Takes user input and tries updating their account's billing information
	 * Returns a client object, which if null, indicates an error with the update.
	 */
	private Client updateAccountBilling(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		// Get current session
		HttpSession session = request.getSession(true);
		
		// Get user input related to updated billing information
		String address = request.getParameter("addressBillingU");
		String province = request.getParameter("provinceBillingU");
		String country = request.getParameter("countryBillingU");
		String zipCode = request.getParameter("zipCodeBillingU");
		String phoneNum = request.getParameter("phoneBillingU");

		// Try updating billing information
		Client client = clientsDAO.updateAccountBilling(String.valueOf(session.getAttribute("userid")), address, province, country, zipCode, phoneNum);
		
		return client;
	}
}
