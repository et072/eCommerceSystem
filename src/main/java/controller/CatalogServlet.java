package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
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

import dao.ItemsDAO;
import dao.ItemsDAOImpl;
import model.Category;
import model.Item;

/**
 * Servlet implementation class CatalogServlet
 * Handles all the filtering and fetching of updated lists for the catalog
 */
@WebServlet("/CatalogServlet")
public class CatalogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ItemsDAO itemsDAO;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CatalogServlet() {
        super();
    }
    
    // Initialize servlet and set application wide attributes for the list of categories, brands, and products
    public void init(ServletConfig config) throws ServletException {
    	super.init(config);
		ServletContext context = config.getServletContext();

		String path = context.getRealPath("/dbFile/items.db");
		context.setAttribute("Items_path", path);		// Set application attribute for access to item DB file path by servlets and JSPs
		itemsDAO = new ItemsDAOImpl();
		
		itemsDAO.setPath(path);			// Set path to items DB file
		
		// Get list of product categories and store them as an application wide attribute for access by servlets and JSPs
		List<Category> categoryList =  itemsDAO.findAllCategories() ;
		context.setAttribute("categoryList", categoryList);
		
		// Get list of product brands and store them as an application wide attribute for access by servlets and JSPs
		List<String> brandList = itemsDAO.findAllBrands();
		context.setAttribute("brandList", brandList);
		
		// Get list of products and store them as an application wide attribute for access by servlets and JSPs
		List<Item> itemList = itemsDAO.findAllItems();
		context.setAttribute("itemList", itemList);
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@SuppressWarnings("unchecked")		// Suppresses warnings relating to converting an attribute object to a list
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get current session
		HttpSession session = request.getSession(true);
		
		List<Item> itemList; 	// List of products

		// Synchronized to prevent concurrent updates
		synchronized (session) {
			// Get list of products, convert attribute from an object to a list of item objects
			itemList = (List<Item>) getServletContext().getAttribute("itemList");
			
			// If there is no list of products, create a new list
			if (itemList == null) {
				itemList = new ArrayList<>();
				session.setAttribute("itemList", itemList);		// Save list of products into session
			}
		}
		
		// Path to catalog page
		String base = "/jsp/";
		String url = base + "Catalog.jsp";
		
		// Get action being taken, product category, and product brand
		String action = request.getParameter("action");
		String category = request.getParameter("category");
		String brand = request.getParameter("brand");
		
		// Represents the key word search
		String keyWord = request.getParameter("keyWord");
		
		// Represents the type of sort: Price ascending, price descending, alpha ascending , alpha descending
		String sortOption = request.getParameter("SortMenu");
		
		// Represents the id of the item the user clicked on
		String itemId = request.getParameter("itemID"); 

		// If there is an action
		if (action != null) {
			switch (action) {
				// Display all catalogue items
				case "allItems":
					// Get all products, set list of products as an attribute
					findAllCatalog(request, response, session);
					
					break;
					
				// Display products of a specified category, set attribute that shows the category used in the filter
				case "category":
					// Set attributes for list of products based on the category, as well as the display message of the filter
					findItemsByCategory(request, response, category, session);
					
					url = base + "Catalog.jsp?category=" + category;	// Path to catalog with category filter
					
					break;
					
				// Display products of a specified brand
				case "brand":
					// Set attributes for list of products based on the brand, as well as the display message of the filter
					findItemsByBrand(request, response, brand, session);
					
					url = base + "Catalog.jsp?brand=" + brand;		// Path to catalog with brand filter
					
					break;			
					
				// Display products based on a keyword search
				case "search":
					// Set attributes for list of products based on the search, as well as display message of the search
					searchItems(request, response, keyWord, session);
					
					url = base + "Catalog.jsp?search=" + keyWord;	// Path to catalog with keyword filter
					
					break;
					
				// Display products based on a specified sort option
				case "sortItems":
					// Set attributes for list of products based on the sort, as well as display message for sorting
					sortItems(request, response,sortOption, session);
					
					url = base + "Catalog.jsp?sorted="+ sortOption;		// Path to catalog that is sorted
					
					break;
					
				// Display specific product page
				case "itemSelected":
					// Set attribute of specified item
					itemSelected(request, response, Long.parseLong(itemId));
					
					url = base + "itemView.jsp?item=" + Long.parseLong(itemId);		// Path to specified item's page
					
					break;
			}
		}
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher(url);
		requestDispatcher.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
	
	private void findAllCatalog(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		try {
			// Get list of all products and set it as an attribute to be passed
			List<Item> itemList = itemsDAO.findAllItems();
			session.setAttribute("itemList", itemList);	
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void findItemsByCategory(HttpServletRequest request, HttpServletResponse response, String category, HttpSession session) throws ServletException, IOException {
		try {
			// Get all products from a specific category and pass the list of products as an attribute
			List<Item> itemList = itemsDAO.findItemsByCategory(category);
			session.setAttribute("itemList", itemList);
			
			// Pass the display message for the category filter as an attribute
			String displayMsg = ("Displaying items from Category: " + category);
			request.setAttribute("displayMsg", displayMsg);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void findItemsByBrand(HttpServletRequest request, HttpServletResponse response, String brand, HttpSession session) throws ServletException, IOException {
		try {
			// Get all products from a specific brand and pass the list of products as an attribute 
			List<Item> itemList = itemsDAO.findItemsByBrand(brand);
			session.setAttribute("itemList", itemList);
			
			// Pass the display message for the brand filter as an attribute
			String displayMsg = ("Displaying items from Brand: " + brand);
			request.setAttribute("displayMsg", displayMsg);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void searchItems(HttpServletRequest request, HttpServletResponse response, String keyWord, HttpSession session) throws ServletException, IOException {
		try {
			// Get all products based on a keyword search and pass the list of products as an attribute
			List<Item> itemList = itemsDAO.searchItemsByKeyword(keyWord);
			session.setAttribute("itemList", itemList);
			
			// Pass the display message for the keyword search as an attribute
			String displayMsg = ("Displaying items from Search: " + keyWord);
			request.setAttribute("displayMsg", displayMsg);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")		// Suppresses warnings relating to converting an attribute object to a list
	private void sortItems(HttpServletRequest request, HttpServletResponse response, String sortOption, HttpSession session) throws ServletException, IOException {
		try {
			// Get list of products through session attribute. Convert attribute from object to a list of items
			List<Item> itemList = (List<Item>) (session.getAttribute("itemList"));
			
			if (itemList != null) {
				String displaySort = null;
				
				// Sort price in ascending order
				if (sortOption.equals("PriceAsc")) {
					// Sort products in ascending order based on price and set new list as an attribute
					Collections.sort(itemList, Comparator.comparing(Item::getPrice));
					session.setAttribute("itemList", itemList);
					
					// Display message
					displaySort = ("Sorting items by: Price Ascending");
				}
				
				// Sort price in descending order
				else if (sortOption.equals("PriceDesc"))  {
					// Sort products in descending order based on price and set new list as an attribute
					Collections.sort(itemList, Comparator.comparing(Item::getPrice));
					Collections.reverse(itemList);
					session.setAttribute("itemList", itemList);
					
					// Display message
					displaySort = ("Sorting items by: Price Descending");
				}
				
				// Sort product name in ascending order
				else if (sortOption.equals("AlphaAsc"))  {
					// Sort products in ascending order based on name and set new list as an attribute
					Collections.sort(itemList, Comparator.comparing(Item::getName));
					session.setAttribute("itemList", itemList);
					
					// Display message
					displaySort = ("Sorting items by: Name Ascending" );
				}
				
				// Sort product name in descending order
				else if (sortOption.equals("AlphaDesc"))  {
					// Sort products in descending order based on name and set new list as an attribute
					Collections.sort(itemList, Comparator.comparing(Item::getName));
					Collections.reverse(itemList);
					session.setAttribute("itemList", itemList);	
					
					// Display message
					displaySort = ("Sorting items by: Name Descending");
				}
				
				request.setAttribute("displaySort", displaySort);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void itemSelected(HttpServletRequest request,HttpServletResponse response, long id) {
		Item item = itemsDAO.getItem(id);		// Get item based on specified ID
		
		request.setAttribute("item", item);		// Pass selected item as an attribute
	}
}
