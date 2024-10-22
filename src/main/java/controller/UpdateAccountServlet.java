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
 */
@WebServlet("/UpdateAccountServlet")
public class UpdateAccountServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ClientsDAO clientsDao; 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public UpdateAccountServlet() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		ServletContext context = config.getServletContext();
		String path = context.getRealPath("/dbFile/client.db");
		clientsDao = new ClientsDAOImpl();

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
		String action = request.getParameter("action");
		String url = "";
		String base = "jsp/";
		
		Client client = null;
		if (action != null) {
			switch (action) {
			case "updateAccountInfo":
				url = base + "account.jsp";
				client = updateAccountInfo(request, response);
				break;
			case "updateAccountShipping":
				url = base + "account.jsp";
				client = updateAccountShipping(request, response);
				break;
			case "updateAccountCC":
				url = base + "account.jsp";
				client = updateAccountCC(request, response);
				break;
			case "updateAccountBilling":
				url = base + "account.jsp";
				client = updateAccountBilling(request, response);
				break;
			}
		}
		
		
		
		if (client == null) {
			request.setAttribute("error", "error");
			url = base + "account.jsp";
		} 
		else {
			setSessionScope(request, response, client);
		}
		RequestDispatcher req = request.getRequestDispatcher(url);
		req.forward(request, response);
	}
	private void setSessionScope(HttpServletRequest request, HttpServletResponse response, Client client) {
		HttpSession session = request.getSession(true);
		
		// Setting session scope attributes
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
	
	private Client updateAccountInfo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		
		String username = request.getParameter("usernameU");
		String password = request.getParameter("passwordU");
		String firstName = request.getParameter("firstNameU");
		String lastName = request.getParameter("lastNameU");
		String email = request.getParameter("emailU");

		Client client = clientsDao.updateAccountInfo(String.valueOf(session.getAttribute("userid")), username, password, firstName, lastName, email);
		
		return client;
	}
	
	private Client updateAccountShipping(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		
		String address = request.getParameter("addressShippingU");
		String province = request.getParameter("provinceShippingU");
		String country = request.getParameter("countryShippingU");
		String zip = request.getParameter("zipCodeShippingU");
		String phone = request.getParameter("phoneShippingU");

		Client client = clientsDao.updateAccountShipping(String.valueOf(session.getAttribute("userid")), address, province, country, zip, phone);
		
		return client;
	}
	
	private Client updateAccountCC(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		
		String cc = request.getParameter("creditCardU");
		String expMon = request.getParameter("ccExpiryMonU");
		String expYr = request.getParameter("ccExpiryYrU");
		String cvv = request.getParameter("cvvU");

		Client client = clientsDao.updateAccountCC(String.valueOf(session.getAttribute("userid")), cc, expMon, expYr, cvv);
		
		return client;
	}
	
	private Client updateAccountBilling(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		HttpSession session = request.getSession(true);
		
		String addr = request.getParameter("addressBillingU");
		String prov = request.getParameter("provinceBillingU");
		String count = request.getParameter("countryBillingU");
		String zipCode = request.getParameter("zipCodeBillingU");
		String phoneNum = request.getParameter("phoneBillingU");

		Client client = clientsDao.updateAccountBilling(String.valueOf(session.getAttribute("userid")), addr, prov, count, zipCode, phoneNum);
		
		return client;
	}

}
