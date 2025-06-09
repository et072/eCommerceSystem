package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/*
 * Object representing the data/state of the shopping cart
 * Possible improvements: Use hashmap instead of list for more efficient access of cart elements
 */

public class Cart {
	private List<Item> cart; // List of items in the cart
	
	// Constructor
	public Cart() {
		cart = new ArrayList<Item>();
	}
	
	// Update the quantity for the item, given its ID.
	public void update(long id, int newQty) {
		for (Item item : cart) {
			if (item.getItemID() == id) {
				item.setQuantity(newQty);
			}
		}
	}
	
	public void remove(long itemID) {
		Iterator<Item> iter = cart.iterator();
		
		// While there are still items in the cart
		while (iter.hasNext()) {
			if (iter.next().getItemID() == itemID) {
				iter.remove();
			}
		}
	}
	
	/*
	 *  Add item to the cart based on the quantity ordered
	 *  If the item ID already exists, update the quantity of items added
	 *  Otherwise, create a new item in the cart.
	 */
	public void add(long itemID, int categoryID, String name, String description, String brand, int quantity, double price) {
		boolean itemExists = false;
		
		// Check if the item exists in the cart. If it does, update its quantity.
		for (Item item : cart) {
			if (item.getItemID() == itemID) {
				itemExists = true;
				
				int newQty = quantity + item.getQuantity();
				update(item.getItemID(), newQty);
			}
		}
		
		// If the item does not exist, add it to the cart
		if (itemExists == false) {
			Item newItem = new Item();
			newItem.setBrand(brand);
			newItem.setCategoryID(categoryID);
			newItem.setDescription(description);
			newItem.setItemID(itemID);
			newItem.setName(name);
			newItem.setPrice(price);
			newItem.setQuantity(quantity);
			
			cart.add(newItem);
		}
	}
	
	// Get the number of items in the cart
	public int size() {
		return cart.size();
	}
	
	// Check if the cart is empty
	public boolean isEmpty() {
		return size() == 0;
	}
	
	// Return all the items as a List<Item>
	public List<Item> getItems() {
		List<Item> items = new ArrayList<>();
		
		for (Item item : cart) {
			Item i = new Item();
			i.setItemID(item.getItemID());
			i.setCategoryID(item.getCategoryID());
			i.setDescription(item.getDescription());
			i.setItemID(item.getItemID());
			i.setName(item.getName());
			i.setPrice(item.getPrice());
			i.setQuantity(item.getQuantity());
			
			items.add(i);
		}
		
		return items;
	}
	
	// Get the total price of the cart
	public double getTotalPrice() {
		double totalPrice = 0;
		for (Item item : cart) {
			totalPrice = totalPrice + (item.getPrice() * item.getQuantity());
		}
		
		return totalPrice;
	}
	
	// Remove all items in the cart
	public void clear() {
		cart.clear();
	}
}
