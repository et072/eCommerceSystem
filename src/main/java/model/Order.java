package model;

import java.util.List;

/*
 * JavaBean for the order information
 * Represents the data/state of each order
 * Includes information like order ID, client ID, date of order, client name, total price, and purchased products.
 */
public class Order {
	private List<Item> items;
	private long clientID;
	private String orderID;
	private String date;
	private String clientFullName;
	private double totalPrice;
	
	public Order() {
		
	}
	
	public List<Item> getItems() {
		return items;
	}
	
	public void setItems(List<Item> items) {
		this.items = items;
	}
	
	public long getClientID() {
		return clientID;
	}
	
	public void setClientID(long custID) {
		this.clientID = custID;
	}
	
	public String getOrderID() {
		return orderID;
	}
	
	public void setOrderID(String orderID) {
		this.orderID = orderID;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getClientFullName() {
		return clientFullName;
	}

	public void setClientFullName(String clientFullName) {
		this.clientFullName = clientFullName;
	}

	public double getTotalPrice() {
		return totalPrice;
	}

	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
