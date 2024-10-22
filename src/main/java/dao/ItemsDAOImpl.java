package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import model.Category;
import model.Item;

public class ItemsDAOImpl implements ItemsDAO {
	
	private String path;
	
	static {
		try {
			Class.forName("org.sqlite.JDBC");
		} 
		catch (ClassNotFoundException ex) {
		}
	}

	private Connection getConnection() throws SQLException {
		 return DriverManager.getConnection(String.format("jdbc:sqlite:%s", this.path));
	}
	
	private void closeConnection(Connection connection) {
		if (connection == null)
			return;
		try {
			connection.close();
		} 
		catch (SQLException ex) {
		}
	}

	public void setPath(String path) {
		this.path = path;
		
	}
	
	@Override
	public List<Item> findAllItems() {
		List<Item> result = new ArrayList<Item>();
		String sql = "select Item.itemID, Item.categoryID, Item.name, Item.description, Item.brand, Item.quantity, Item.price, Category.Category from Item  inner  join Category on  Item.categoryID = Category.ID ";
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet =  statement.executeQuery();
			while (resultSet.next()){
				
				Long id = resultSet.getLong("itemID");
				int catId = resultSet.getInt("categoryID");
				String name = resultSet.getString("name");
				String desc = resultSet.getString("description");
				String brand = resultSet.getString("brand");
				int quantity = resultSet.getInt("quantity");
				int price = resultSet.getInt("price");
				
				Item i = new Item(id,catId,name,desc,brand,quantity,price); 				
				result.add(i);
			}
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return result;
	}

	@Override
	public List<Item> searchItemsByKeyword(String keyWord) {
		List<Item> result = new ArrayList<Item>();
		
		String sql = "select * from Item inner join Category on  Item.categoryId = Category.id"
				+ " where name like '%"
				+ keyWord.trim()
				+ "%'"
				+ " or description like '%"
				+ keyWord.trim()
				+ "%'"
				+ " or Category like '%"
				+ keyWord.trim()
				+ "%'"
				+ " or brand like '%" + keyWord.trim() + "%'";
		
		Connection connection = null;
		try {

			connection = getConnection();
			PreparedStatement statement =  connection.prepareStatement(sql);
			ResultSet resultSet =  statement.executeQuery(); 
			while (resultSet.next()) {
				Long id = resultSet.getLong("itemID");
				int catId = resultSet.getInt("categoryID");
				String name = resultSet.getString("name");
				String desc = resultSet.getString("description");
				String brand = resultSet.getString("brand");
				int quantity = resultSet.getInt("quantity");
				int price = resultSet.getInt("price");
				
				Item i = new Item(id,catId,name,desc,brand,quantity,price); 				
				result.add(i);
				
			}
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}

		return result;

	}

	@Override
	public List<Category> findAllCategories() {
		List<Category> result = new ArrayList<>();
		String sql = "select * from Category";

		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery(); 
			while (resultSet.next()) {
				Category category = new Category();
				
				// Populate category bean with needed info
				category.setId(resultSet.getLong("ID"));
	            category.setCategoryDescription(resultSet.getString("Category"));
				result.add(category);
			}
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return result;
		
	}
	
	@Override
	// Used to get a list of all brands for filtering catalog
	public List<String> findAllBrands() {
		List<String> result = new ArrayList<>();
		String sql = "select DISTINCT Brand from Item";
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery(); 
			while (resultSet.next()) {
				String b =resultSet.getString("brand");
				result.add(b);
			}
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return result;
	}
	
	@Override
	public List<Item> findItemsByCategory(String category) {
		List<Item> result = new ArrayList<Item>();
		String sql = "select Item.itemID, Item.categoryID, Item.name, Item.description, Item.brand, Item.quantity, Item.price, Category.Category from Item  inner  join Category on  Item.categoryID = Category.ID where "
				+ "Category=?";
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, category);
			ResultSet resultSet =  statement.executeQuery(); 
			while (resultSet.next()) {

				Long id = resultSet.getLong("itemID");
				int catId = resultSet.getInt("categoryID");
				String name = resultSet.getString("name");
				String desc = resultSet.getString("description");
				String brand = resultSet.getString("brand");
				int quantity = resultSet.getInt("quantity");
				int price = resultSet.getInt("price");
				
				Item i = new Item(id,catId,name,desc,brand,quantity,price); 				
				result.add(i);
			}
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return result;
	}
	
	
	@Override
	public List<Item> findItemsByBrand(String brand) {
		List<Item> result = new ArrayList<Item>();
		String sql = "select Item.itemID, Item.categoryID, Item.name, Item.description, Item.brand, Item.quantity, Item.price, Category.Category from Item  inner  join Category on  Item.categoryID = Category.ID where "
				+ "brand=?";
		
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, brand);
			ResultSet resultSet =  statement.executeQuery(); 
			while (resultSet.next()) {

				Long id = resultSet.getLong("itemID");
				int catId = resultSet.getInt("categoryID");
				String name = resultSet.getString("name");
				String desc = resultSet.getString("description");
				String item_brand = resultSet.getString("brand");
				int quantity = resultSet.getInt("quantity");
				int price = resultSet.getInt("price");
				
				Item i = new Item(id,catId,name,desc,item_brand,quantity,price); 				
				result.add(i);
			}
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
		return result;
	}
	
	
	@Override
	public void insert(Item item) {
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(
					"insert into Items (name) values (?)",
					Statement.RETURN_GENERATED_KEYS);
			statement.setString(1, item.getName());
			statement.execute();
			ResultSet generatedKeys = statement.getGeneratedKeys();
			if (generatedKeys.next()) {
				item.setItemID(generatedKeys.getLong(1));
			}
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
	}

	@Override
	public void delete(long itemId) {
		Connection connection = null;

		try {
			connection = getConnection();
			PreparedStatement statement = connection
					.prepareStatement("delete from items where id=?");
			statement.setLong(1, itemId);
			statement.execute();
		} 
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		}
	}

	@Override
	public Item getItem(long id) {
			
		String sql = "select Item.itemID, Item.categoryID, Item.name, Item.description, Item.brand, Item.quantity, Item.price from Item where "
				+ "itemId=?";
		Item i = new Item();
		Connection connection = null;
		try {
			connection = getConnection();
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			ResultSet resultSet =  statement.executeQuery(); 
			while (resultSet.next()) {
				i.setItemID(id);
				i.setCategoryID(resultSet.getInt("categoryID"));
				i.setName(resultSet.getString("name"));
				i.setDescription(resultSet.getString("description"));
				i.setBrand(resultSet.getString("brand"));
				i.setPrice(resultSet.getInt("price"));
				i.setQuantity(resultSet.getInt("quantity"));
			}	
		}
		catch (SQLException ex) {
			ex.printStackTrace();
		} 
		finally {
			closeConnection(connection);
		
		}
		return i;
	}
}
