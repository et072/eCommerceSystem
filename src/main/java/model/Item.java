package model;

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
	public Item(long itemID, int categoryID, String name, String description, String brand, int quantity, double price) {
		super();
		this.itemID = itemID;
		this.categoryID = categoryID;
		this.name = name;
		this.description = description;
		this.brand = brand;
		this.quantity = quantity;
		this.price = price;
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
