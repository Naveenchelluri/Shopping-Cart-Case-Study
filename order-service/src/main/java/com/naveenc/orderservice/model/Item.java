package com.naveenc.orderservice.model;

public class Item {
	int id;
	String image;
	String prodName;
	double price;
	int quantity;
	
	public Item() {}

	public Item(int id, String image, String prodName, double price, int quantity) {
		super();
		this.id = id;
		this.image = image;
		this.prodName = prodName;
		this.price = price;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getProdName() {
		return prodName;
	}

	public void setProdName(String prodName) {
		this.prodName = prodName;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", image=" + image + ", prodName=" + prodName + ", price=" + price + ", quantity="
				+ quantity + "]";
	}

}
