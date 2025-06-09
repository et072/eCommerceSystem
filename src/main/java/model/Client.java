package model;

public class Client {
	private long ID;
	private String status;
	private String username;
	private String password;
	private String firstName;
	private String lastName;
	private String email;
	private Shipping shipping;
	private Billing billing;
	
	public Client() {
		
	}
	
	public long getID() {
		return ID;
	}
	
	public void setID(long id) {
		ID = id;
	}
	
	public String getStatus() {
		return status;
	}
	
	public void setStatus(String status) {
		this.status = status;
	}
	
	public String getUsername() {
		return username;
	}
	
	public void setUsername(String username) {
		this.username = username;
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getFirstName() {
		return firstName;
	}
	
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	
	public String getLastName() {
		return lastName;
	}
	
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public Shipping getShipping() {
		return shipping;
	}
	
	public void setShipping(Shipping shipping) {
		this.shipping = shipping;
	}
	
	public Billing getBilling() {
		return billing;
	}
	
	public void setBilling(Billing billing) {
		this.billing = billing;
	}
}
