package model;

/*
 * JavaBean for the products on the website.
 * Represents the data/state of website products.
 * Includes information like item ID, associated category ID, name, description, brand, price, and quantity.
 */
public class Item {
	private long itemID;
	private int categoryID;
	private String name;
	private String description;
	private String brand;
	private int quantity;
	private double price;
	
	public Item() {
		
	}
	
	public long getItemID() {
		return itemID;
	}
	
	public void setItemID(long itemID) {
		this.itemID = itemID;
	}
	
	public int getCategoryID() {
		return categoryID;
	}
	
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getDescription() {
		return description;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getBrand() {
		return brand;
	}
	
	public void setBrand(String brand) {
		this.brand = brand;
	}
	
	public int getQuantity() {
		return quantity;
	}
	
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
}
