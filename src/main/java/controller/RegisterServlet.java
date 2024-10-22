package controller;

import java.io.IOException;

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
import model.Client;

/**
 * Servlet implementation class RegisterServlet
 */
@WebServlet("/RegisterServlet")
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ClientsDAO clientsDao;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterServlet() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		String path = context.getRealPath("/dbFile/client.db");
		clientsDao= new ClientsDAOImpl();

		clientsDao.setPath(path);
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
		
		HttpSession session = request.getSession(true);

		
		try {
			Client client = clientsDao.register(request);

			if (client != null) {
				setSessionScope(request, response, client);
				
				String redirect = String.valueOf(session.getAttribute("redirect"));
				
				if (redirect.equalsIgnoreCase("redirect")) {
					url = base + "checkOut.jsp";
					session.setAttribute("redirect", "");
				} 
				else {
					url = base + "account.jsp";
				}
				
			} 
			else {
				request.setAttribute("error", "error");
				url = base + "register.jsp";
			}
			
			session.setAttribute("loggedIn", "yes");
			// Default list of admins
			session.setAttribute("status", "client"); 
		}
		catch (Exception e) {

		}

		RequestDispatcher req = request.getRequestDispatcher(url);
		req.forward(request, response);
	} 



	private void setSessionScope(HttpServletRequest request, HttpServletResponse response, Client client) {
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
