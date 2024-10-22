package model;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Cart {
 
   private List<Item> cart;  // List of Items
 
   // Constructor
   public Cart() {
      cart = new ArrayList<Item>();
   }
   
         
   // Add a Item into this Cart, with qtyOrdered. If the Item of id already exists, update the qty ordered
   // If not, create a new Item.
   public void add(long itemID, int categoryID, String name, String description, String brand, int quantity, double price) {
	   boolean ItemExists =false; 
	   for (Item b: cart) {
		   if (b.getItemID()==itemID) {
			   ItemExists=true;
			   int newQty = quantity + b.getQuantity();
			   update(b.getItemID(),newQty);
		   }   
	   }
	   
	   if (ItemExists == false) {
		   Item newItem = new Item (itemID,categoryID, name, description,  brand,  quantity,  price);
		   newItem.setQuantity(quantity);
		   cart.add(newItem);
	   }   
   }
 
   // Update the quantity for the given id, of total orderqty
   public void update(long id, int newQty) {
	   for (Item b: cart) {
		   if (b.getItemID()==id) {
			   b.setQuantity(newQty);;
			   
		   }   
	   }
   }
 
   // Remove a Item given its id
   public void remove(long itemId) {
      Iterator<Item> iter = cart.iterator();
      while (iter.hasNext()) {
    	  if (iter.next().getItemID()==itemId) {
    		  iter.remove();
    	  }    
      }
   }
 
   // Get the number of Items in this Cart
   public int size() {
      return cart.size();
   }
 
   // Check if this Cart is empty
   public boolean isEmpty() {
      return size() == 0;
   }
 
   // Return all the Items in a List<Item>
   public List<Item> getItems() {
	  List<Item> items = new ArrayList<>();
      for (Item item : cart) {
    	  Item i = new Item();
    	  i.setItemID(item.getItemID());
    	  i.setCategoryID(item.getCategoryID());
    	  i.setName(item.getName());
    	  i.setDescription(item.getDescription());
    	  i.setBrand(item.getBrand());
    	  i.setQuantity(item.getQuantity());
    	  i.setPrice(item.getPrice());
    	  
    	  items.add(i);
      }
	  return items;
   }
   public double getTotalPrice() {
	  double totalPrice=0;
	   for (Item i :cart) {
		   totalPrice= totalPrice +(i.getPrice()*i.getQuantity());
	   }
	   return totalPrice;
   }
 
   // Remove all the s in this Cart
   public void clear() {
      cart.clear();
   }
}