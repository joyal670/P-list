package com.ashtechlabs.teleporter.ui.pricing;

public class RateCardDriver {

	public String location_from;
	public String location_to;
	public String item_type;
	public String minAmount;
	public String perKgAmount;
	public String duration;
	public int category;
	
	public RateCardDriver(String location_from,String location_to,String item_type,String minAmount,String perKgAmount,String duration, int category) {
		// TODO Auto-generated constructor stub
		this.location_from=location_from;
		this.location_to=location_to;
		this.item_type=item_type;
		this.minAmount=minAmount;
		this.perKgAmount=perKgAmount;
		this.duration=duration;
		this.category=category;

	}
}
