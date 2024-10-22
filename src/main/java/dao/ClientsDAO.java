package dao;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.Client;

public interface ClientsDAO {
	
	public Client login(String username, String password);
	
	public void setPath(String path);
	
	public Client register(HttpServletRequest request);
	
	public Client updateAccountInfo(String id, String username, String password, String firstName, String lastName, String email);
	
	public Client updateAccountShipping(String id, String strAddr, String province, String country, String zip, String phoneNum);
	
	public Client updateAccountCC(String id, String cc, String expMon, String expYr, String cvv);
	
	public Client updateAccountBilling(String id, String address, String province, String country, String zip, String phone);
	
	public String getStatus(String id);
	
	public List<Client> getAccounts();
	
	public Client login(String id);
}
