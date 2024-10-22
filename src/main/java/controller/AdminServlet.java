package controller;

import java.io.IOException;
import java.util.ArrayList;
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
import model.Category;
import model.Client;
import model.Item;
import model.Order;

/**
 * Servlet implementation class AdminServlet
 */
@WebServlet("/AdminServlet")
public class AdminServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	ItemsDAO itemsDao;
	ClientsDAO clientsDao;
	OrdersDAO ordersDao;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AdminServlet() {
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
		String url = "";
		String base = "jsp/";
		url = base + "admin.jsp";
		HttpSession session = request.getSession(true);

		

		String clientID = request.getParameter("clientIdA");	// Client ID of the account being updated
		if (action != null) {
			switch (action) {
			case "updateAccountInfo":
				url = base + "admin.jsp";
				// Get updated account information parameters and update the account info
				String username = request.getParameter("aiu" + clientID);
				String password = request.getParameter("aip"+clientID);
				String firstName = request.getParameter("aif"+clientID);
				String lastName = request.getParameter("ail"+clientID);
				String email = request.getParameter("aie"+clientID);

				clientsDao.updateAccountInfo(clientID, username, password, firstName, lastName, email);

				break;
			case "updateAccountShipping":
				url = base + "admin.jsp";
				// Get updated shipping information parameters and update the shipping account info				
				String shippingAddress = request.getParameter("sa"+clientID);
				String shippingProvince = request.getParameter("spr"+clientID);
				String shippingCountry = request.getParameter("sc"+clientID);
				String shippingZip = request.getParameter("sz"+clientID);
				String shippingPhone = request.getParameter("sph"+clientID);
				clientsDao.updateAccountShipping(clientID, shippingAddress, shippingProvince, shippingCountry, shippingZip, shippingPhone);

				break;
			case "updateAccountCC":
				url = base + "admin.jsp";
				// Get updated credit card information parameters and update the credit card account info
				String cc = request.getParameter("cc"+clientID);
				String ccExpiryMon = request.getParameter("ccem"+clientID);
				String ccExpiryYr = request.getParameter("ccey"+clientID);
				String cvv = request.getParameter("ccv"+clientID);
				clientsDao.updateAccountCC(clientID, cc, ccExpiryMon, ccExpiryYr, cvv);

				break;
			case "updateAccountBilling":
				url = base + "admin.jsp";
				// Get updated billing information parameters and update the billing account info
				String billingAddress = request.getParameter("ba"+clientID);
				String billingProvince = request.getParameter("bpr"+clientID);
				String billingCountry = request.getParameter("bc"+clientID);
				String billingZip = request.getParameter("bz"+clientID);
				String billingPhone = request.getParameter("bph"+clientID);
				clientsDao.updateAccountBilling(clientID, billingAddress, billingProvince, billingCountry, billingZip, billingPhone);
				break;
			case "account":
				url = base + "admin.jsp";
				break;
			case "order":
				// Get sales history
				url = base + "orderPreview.jsp";
				List<Order> orders = ordersDao.getOrders(String.valueOf(session.getAttribute("userid")), clientsDao, itemsDao);
				request.setAttribute("orders", orders);
				double ordersTotalPrice = 0.0;
				for(Order order: orders) {
					ordersTotalPrice += order.getTotalPrice();
				}
				request.setAttribute("ordersTotalPrice", ordersTotalPrice);
				break;
			case "item":
				url = base + "adminList.jsp";
				List<Item> allItems = itemsDao.findAllItems();
				request.setAttribute("itemList", allItems);
				break;
			}
		}

		// View user accounts. Can update user information
		List<Client> allAccounts = clientsDao.getAccounts();
		request.setAttribute("accountList", allAccounts);

		RequestDispatcher req = request.getRequestDispatcher(url);
		req.forward(request, response);
	}

}