package dao;

import java.util.List;

import model.Category;
import model.Item;

/*
 * Provides an interface to the database for product data.
 * Allows for web application to communicate with the database to search for store products
 */

public interface ItemsDAO {
	public List<Item> findAllItems();
	
	public List<Item> searchItemsByKeyword(String keyWord);
	
	public List<Category> findAllCategories();
	
	public List<String> findAllBrands();

	public void insert(Item item);

	public void setPath(String path);
	
	public void delete(long itemId);
	
	public List<Item> findItemsByCategory(String category);
	
	public List<Item> findItemsByBrand(String brand);
	
	public Item getItem(long id);
}
