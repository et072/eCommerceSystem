package model;

/*
 * JavaBean for shipping information
 * Represents the data/state of shipping information
 */
public class Shipping {
	private String addressS;
	private String provinceS;
	private String countryS;
	private String zipS;
	private long phoneS;
	
	public Shipping() {
		
	}
	
	public String getAddressS() {
		return addressS;
	}
	
	public void setAddressS(String addressS) {
		this.addressS = addressS;
	}
	
	public String getProvinceS() {
		return provinceS;
	}
	
	public void setProvinceS(String provinceS) {
		this.provinceS = provinceS;
	}
	
	public String getCountryS() {
		return countryS;
	}
	
	public void setCountryS(String countryS) {
		this.countryS = countryS;
	}
	
	public String getZipS() {
		return zipS;
	}
	
	public void setZipS(String zipS) {
		this.zipS = zipS;
	}
	
	public long getPhoneS() {
		return phoneS;
	}
	
	public void setPhoneS(long phoneS) {
		this.phoneS = phoneS;
	}
}
