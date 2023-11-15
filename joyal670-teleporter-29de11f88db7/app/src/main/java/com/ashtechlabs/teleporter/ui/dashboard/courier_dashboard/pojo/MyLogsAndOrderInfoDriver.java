package com.ashtechlabs.teleporter.ui.dashboard.courier_dashboard.pojo;


import android.os.Parcel;
import android.os.Parcelable;


public class MyLogsAndOrderInfoDriver implements Parcelable{

	private String id;
	private String ordertype;
	private String orderid;
	private String ratecard;
	private String state;
	private String itemtype;
	private String fromlocation;
	private String tolocation;
	private String date;
	private String time;
	private String commodity_description;
	private String payment;
	private double Weight;
	private String weight_unit;
	private String order_number;
	private String price;
	private String Duration;
	private int currency;


	public MyLogsAndOrderInfoDriver(String id, String time, String date, String fromlocation, String tolocation, String state, String itemtype, String orderid, String ordertype,
	String commoditydescription ,String payment,double Weight,String weight_unit,String order_number,String price,int currency, String Duration ) {
		// TODO Auto-generated constructor stub
		this.id=id;
		this.time=time;
		this.date=date;
		this.fromlocation=fromlocation;
		this.tolocation=tolocation;
		this.state=state;
		this.itemtype=itemtype;
		this.orderid=orderid;
		this.ordertype=ordertype;
		this.commodity_description=commoditydescription;
		this.payment=payment;
		this.Weight=Weight;
		this.weight_unit=weight_unit;
		this.order_number=order_number;
		this.price=price;
		this.Duration=Duration;
		this.currency=currency;
	}

	protected MyLogsAndOrderInfoDriver(Parcel in) {
		id = in.readString();
		ordertype = in.readString();
		orderid = in.readString();
		ratecard = in.readString();
		state = in.readString();
		itemtype = in.readString();
		fromlocation = in.readString();
		tolocation = in.readString();
		date = in.readString();
		time = in.readString();
		commodity_description = in.readString();
		payment = in.readString();
		Weight = in.readDouble();
		weight_unit = in.readString();
		order_number = in.readString();
		price = in.readString();
		Duration = in.readString();
		currency = in.readInt();
	}

	public static final Creator<MyLogsAndOrderInfoDriver> CREATOR = new Creator<MyLogsAndOrderInfoDriver>() {
		@Override
		public MyLogsAndOrderInfoDriver createFromParcel(Parcel in) {
			return new MyLogsAndOrderInfoDriver(in);
		}

		@Override
		public MyLogsAndOrderInfoDriver[] newArray(int size) {
			return new MyLogsAndOrderInfoDriver[size];
		}
	};

	public String getId() {
		return id;
	}

	public String getOrdertype() {
		return ordertype;
	}

	public String getOrderid() {
		return orderid;
	}

	public String getRatecard() {
		return ratecard;
	}

	public String getState() {
		return state;
	}

	public String getItemtype() {
		return itemtype;
	}

	public String getFromlocation() {
		return fromlocation;
	}

	public String getTolocation() {
		return tolocation;
	}

	public String getDate() {
		return date;
	}

	public String getTime() {
		return time;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCommodity_description() {
		return commodity_description;
	}

	public void setCommodity_description(String commodity_description) {
		this.commodity_description = commodity_description;
	}

	public String getPayment() {
		return payment;
	}

	public void setPayment(String payment) {
		this.payment = payment;
	}

	public double getWeight() {
		return Weight;
	}

	public void setWeight(double weight) {
		Weight = weight;
	}

	public String getWeight_unit() {
		return weight_unit;
	}

	public void setWeight_unit(String weight_unit) {
		this.weight_unit = weight_unit;
	}

	public String getOrder_number() {
		return order_number;
	}

	public void setOrder_number(String order_number) {
		this.order_number = order_number;
	}

	public String getPrice() {
		return price;
	}

	public void setPrice(String price) {
		this.price = price;
	}

	public String getDuration() {
		return Duration;
	}

	public void setDuration(String duration) {
		Duration = duration;
	}

	public int getCurrency() {
		return currency;
	}

	public void setCurrency(int currency) {
		this.currency = currency;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(ordertype);
		dest.writeString(orderid);
		dest.writeString(ratecard);
		dest.writeString(state);
		dest.writeString(itemtype);
		dest.writeString(fromlocation);
		dest.writeString(tolocation);
		dest.writeString(date);
		dest.writeString(time);
		dest.writeString(commodity_description);
		dest.writeString(payment);
		dest.writeDouble(Weight);
		dest.writeString(weight_unit);
		dest.writeString(order_number);
		dest.writeString(price);
		dest.writeString(Duration);
		dest.writeInt(currency);


	}
}
