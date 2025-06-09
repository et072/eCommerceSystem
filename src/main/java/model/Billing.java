package model;


/*
 * JavaBean for the billing information.
 * Represents the data/state of payment information entered by clients.
 * Handles information like credit card data and billing information.
 */
public class Billing {
	private long cc;
	private int ccExpiryMon;
	private int ccExpiryYr;
	private int cvv;
	private String addressB;
	private String provinceB;
	private String countryB;
	private String zipB;
	private long phoneB;
	
	public Billing() {
		
	}
	
	public long getCc() {
		return cc;
	}
	
	public void setCc(long cc) {
		this.cc = cc;
	}
	
	public int getCcExpiryMon() {
		return ccExpiryMon;
	}
	
	public void setCcExpiryMon(int ccExpiryMon) {
		this.ccExpiryMon = ccExpiryMon;
	}
	
	public int getCcExpiryYr() {
		return ccExpiryYr;
	}
	
	public void setCcExpiryYr(int ccExpiryYr) {
		this.ccExpiryYr = ccExpiryYr;
	}
	
	public int getCvv() {
		return cvv;
	}
	
	public void setCvv(int cvv) {
		this.cvv = cvv;
	}
	
	public String getAddressB() {
		return addressB;
	}
	
	public void setAddressB(String addressB) {
		this.addressB = addressB;
	}
	
	public String getProvinceB() {
		return provinceB;
	}
	
	public void setProvinceB(String provinceB) {
		this.provinceB = provinceB;
	}
	
	public String getCountryB() {
		return countryB;
	}
	
	public void setCountryB(String countryB) {
		this.countryB = countryB;
	}
	
	public String getZipB() {
		return zipB;
	}
	
	public void setZipB(String zipB) {
		this.zipB = zipB;
	}
	
	public long getPhoneB() {
		return phoneB;
	}
	
	public void setPhoneB(long phoneB) {
		this.phoneB = phoneB;
	}
}
