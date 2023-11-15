package com.ashtechlabs.teleporter.ui.dashboard.storage_dashboard.pojo;

public class MyLogsOrderInfoWareHouse {

	public String order_number;
	public String time;
	public String date;
	public String location_from;
	public String location_to;
	public String state;
	public String item_type;
	public String jobID;
	public String orderType;

	public MyLogsOrderInfoWareHouse(String order_number, String time, String date, String location_from, String location_to, String state, String item_type, String jobID, String orderType) {
		// TODO Auto-generated constructor stub
		this.order_number=order_number;
		this.time=time;
		this.date=date;
		this.location_from=location_from;
		this.location_to=location_to;
		this.state=state;
		this.item_type=item_type;
		this.jobID=jobID;
		this.orderType=orderType;
	}

}
