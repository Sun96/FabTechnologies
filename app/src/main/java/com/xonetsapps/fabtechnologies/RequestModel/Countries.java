package com.xonetsapps.fabtechnologies.RequestModel;

public class Countries {
public String name;
	public String date;
	public String order;

	public String id,status;
	public String price;

	public Countries(String id,String date, String price,String status,String order){
		this.id=id;
		this.order=order;
		this.date=date;
		this.price=price;
		this.status=status;
	}

	public String getID() {
		return id;
	}

	public void setID(String id) {
		id = id;
	}

	public void setStatus(String status) {
		status = status;
	}

	public String getStatus() {
		return status;
	}


	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		date = date;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}
	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

}
