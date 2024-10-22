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

import dao.ItemsDAOImpl;
import dao.ItemsDAO;
import model.Category;
import model.Item;

/**
 * Servlet implementation class CatalogServlet
 */
@WebServlet("/CatalogServlet")
public class CatalogServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ItemsDAO itemsDao;   
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CatalogServlet() {
        super();
    }
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		ServletContext context = config.getServletContext();

		String path = context.getRealPath("/dbFile/items.db");
		context.setAttribute("Items_path", path);
		itemsDao = new ItemsDAOImpl(); 

		itemsDao.setPath(path);
		
		List<Category> categoryList =  itemsDao.findAllCategories() ;
		context.setAttribute("categoryList", categoryList);
		
		
		List<String> brandList = itemsDao.findAllBrands();
		context.setAttribute("brandList", brandList);
		
		List<Item> itemList = itemsDao.findAllItems();
		context.setAttribute("itemList", itemList);
	}
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		HttpSession session = request.getSession(true);
		List<Item> itemList;
		synchronized (session) { // Synchronized to prevent concurrent updates
			// Retrieve the shopping cart for this session, if any. Otherwise, create one.
			itemList = (List<Item>) getServletContext().getAttribute("itemList");
			
			if (itemList == null) { // If no cart exists, create one.
				itemList = new ArrayList<>();
				session.setAttribute("itemList", itemList); // Save cart into the session
			}
		}	
		String base = "/jsp/";
		String action = request.getParameter("action");
		String category = request.getParameter("category");
		String brand = request.getParameter("brand");
		String keyWord = request.getParameter("keyWord");
		String sortOption = request.getParameter("SortMenu"); // Represents what kind of sort: Price ascending, price descending, alpha ascending , alpha descending
		String itemId = request.getParameter("itemID"); //Represents the item id of the item the user clicked on 
		String url = base + "Catalog.jsp"; 
		if (action != null) {
			switch (action) {
			case "allItems":
				findAllCatalog(request, response,session);
				break;
			case "category":
				findItemsByCategory(request, response, category,session);
				url = base + "Catalog.jsp?category=" + category;
				break;
			case "brand":
				findItemsByBrand(request, response, brand,session);
				url = base + "Catalog.jsp?brand=" + brand;
				break;				
			case "search":
				searchItems(request, response, keyWord,session);
				url = base + "Catalog.jsp?search=" + keyWord;
				break;
			case "sortItems":
				sortItems(request, response,sortOption,session);
				url = base + "Catalog.jsp?sorted="+ sortOption;
				break;
			case "itemSelected":
				itemSelected(request, response,Long.parseLong(itemId));
				url = base + "ItemView.jsp?item=" + Long.parseLong(itemId);
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
	
	private void findAllCatalog(HttpServletRequest request,
			HttpServletResponse response, HttpSession session) throws ServletException, IOException {
		try {
			List<Item> itemList = itemsDao.findAllItems();
			session.setAttribute("itemList", itemList);		
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	private void findItemsByCategory(HttpServletRequest request,
			HttpServletResponse response, String cate, HttpSession session)
			throws ServletException, IOException {
		try {
			List<Item> itemList = itemsDao.findItemsByCategory(cate);
			session.setAttribute("itemList", itemList);
			String displayMsg = ("Displaying items from Category: "+cate);
			request.setAttribute("displayMsg", displayMsg);
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	private void findItemsByBrand(HttpServletRequest request,
			HttpServletResponse response, String brnd, HttpSession session)
			throws ServletException, IOException {
		try {
			List<Item> itemList = itemsDao.findItemsByBrand(brnd);
			session.setAttribute("itemList", itemList);
			String displayMsg = ("Displaying items from Brand: "+brnd);
			request.setAttribute("displayMsg", displayMsg);

		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	private void searchItems(HttpServletRequest request,
			HttpServletResponse response, String keyWord, HttpSession session)
			throws ServletException, IOException {
		try {
			List<Item> itemList = itemsDao.searchItemsByKeyword(keyWord);
			session.setAttribute("itemList", itemList);
			String displayMsg = ("Displaying items from Search: "+keyWord);
			request.setAttribute("displayMsg", displayMsg);

		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	@SuppressWarnings("unchecked")
	private void sortItems(HttpServletRequest request,
			HttpServletResponse response, String sortOption, HttpSession session) 
			throws ServletException, IOException {
		try {
		
			List<Item> itemList = (List<Item>) (session.getAttribute("itemList"));

			if(itemList != null) {
				String displaySort=null;
				if(sortOption.equals("PriceAsc")) { 	//Sort by price ascending
					Collections.sort(itemList, Comparator.comparing(Item::getPrice));
					session.setAttribute("itemList", itemList);
					displaySort = ("Sorting items by: Price Ascending");
				}
				else if(sortOption.equals("PriceDesc"))  {	//Sort by price descending
					Collections.sort(itemList, Comparator.comparing(Item::getPrice));
					Collections.reverse(itemList);
					session.setAttribute("itemList", itemList);
					displaySort = ("Sorting items by: Price Descending");
				}
				else if(sortOption.equals("AlphaAsc"))  {	//Sort by alpha ascending
					Collections.sort(itemList, Comparator.comparing(Item::getName));
					session.setAttribute("itemList", itemList);
					displaySort = ("Sorting items by: Name Ascending" );
				}
				else if(sortOption.equals("AlphaDesc"))  {	//Sort by alpha descending
					Collections.sort(itemList, Comparator.comparing(Item::getName));
					Collections.reverse(itemList);
					session.setAttribute("itemList", itemList);	
					displaySort = ("Sorting items by: Name Descending");
				}
				request.setAttribute("displaySort", displaySort);
			}
			
		} 
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	
	private void itemSelected(HttpServletRequest request,HttpServletResponse response, long id) {
		Item i = itemsDao.getItem(id);
		request.setAttribute("item", i);
	}
	
	
	

}
