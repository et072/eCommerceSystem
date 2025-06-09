package model;

/*
 * JavaBean for the product categories
 * Represents the data/state of product categories
 * Includes ID of the category and its description.
 */

public class Category {
	private Long id;	// ID of the category, used to access category from DB
	private String categoryDescription;
	
	public Category() {
		
	}
	
	public Long getId() {
		return id;
	}
	
	public void setId(Long id) {
		this.id = id;
	}
	
	public String getCategoryDescription() {
		return categoryDescription;
	}
	
	public void setCategoryDescription(String categoryDescription) {
		this.categoryDescription = categoryDescription;
	}
	
	// Output the ID and description of the category
	public String toString() {
		return "Category - Id: " + id + ", Category Description: " + categoryDescription;
	}
}
