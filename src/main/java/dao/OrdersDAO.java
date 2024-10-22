package dao;

import java.util.List;

import model.Cart;
import model.Order;

public interface OrdersDAO {
	public void setPath(String path);
	
	public boolean createOrder(Cart cart, String clientId);
	
	public List<Order> getOrders(String clientID, ClientsDAO clientDao, ItemsDAO itemDao);
}
