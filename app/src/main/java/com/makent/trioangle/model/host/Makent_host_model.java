package com.makent.trioangle.model.host;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  model/host
 * @category    Makent_host_model
 * @author      Trioangle Product Team
 * @version     1.1
 */

import java.io.Serializable;

public class Makent_host_model implements Serializable {
	private String listing_room_image,roomid,roomname,roomtype,roomprice,roomlocation,roomstepcount,roomdetails;
	private String id,moreproperty;
	public String type;
	private String amenities,amenities_image,name,countryname,currencysymbol,currencyname,paypalemailid,isdefault,payoutid,payoutmethod;


	public Makent_host_model() {

	}
	public Makent_host_model(String type) {
		this.type = type;
	}
	public Makent_host_model(String type,String listing_room_image,String roomid,String roomdetails,String roomname,String roomtype,String roomprice,String roomlocation,String roomstepcount,String id,String moreproperty,String currencySymbol,String currencyname,String paypalemailid,
							 String isdefault,String payoutid,String payoutmethod) {
		this.type=type;
		this.listing_room_image=listing_room_image;
		this.roomid=roomid;
		this.roomname=roomname;
		this.roomtype=roomtype;
		this.roomlocation=roomlocation;
		this.roomstepcount=roomstepcount;
		this.id=id;
		this.moreproperty=moreproperty;
		this.currencysymbol=currencysymbol;
		this.currencyname=currencyname;
		this.paypalemailid=paypalemailid;
		this.isdefault=isdefault;
		this.payoutid=payoutid;
		this.payoutmethod=payoutmethod;
		this.roomprice=roomprice;
		this.roomdetails=roomdetails;

	}
	 //Detailed list space
	 
	public String getlisting_room_image() {	return listing_room_image;	}

	public void setlisting_room_image(String listing_room_image) {	this.listing_room_image = listing_room_image;}

	public String getRoomId() {
		return roomid;
	}

	public void setRoomId(String roomid) {	this.roomid = roomid;	}

	public String getRoomname() {
		return roomname;
	}

	public void setRoomname(String roomname) {	this.roomname = roomname;	}

	public String getRoomType() {
		return roomtype;
	}

	public void setRoomType(String roomtype) {	this.roomtype = roomtype;	}

	public String getRoomDetails() { return roomdetails;	}

	public void setRoomDetails(String roomdetails) {	this.roomdetails = roomdetails;	}

	public String getRoomPrice() { return roomprice;	}

	public void setRoomPrice(String roomprice) {	this.roomprice = roomprice;	}

	public String getRoomLocation() {
		return roomlocation;
	}

	public void setRoomLocation(String roomlocation) {	this.roomlocation = roomlocation;	}

	public String getRoomStepCount() {	return roomstepcount; }

	public void setRoomStepCount(String roomstepcount) {	this.roomstepcount = roomstepcount;	}


	public String getid() {
		return id;
	}

	public void setid(String id) {	this.id = id;	}

	public String getMoreProerty() {
		return moreproperty;
	}

	public void setMoreProperty(String moreproperty) {	this.moreproperty = moreproperty;	}

	public String getCurrencyName() {
		return currencyname;
	}

	public void setCurrencyName(String currencyname) {	this.currencyname = currencyname;	}

	public String getCurrencySymbol() {
		return currencysymbol;
	}

	public void setCurrencySymbol(String currencysymbol) {	this.currencysymbol = currencysymbol;	}

	public String getPayPalEmailId() {
		return paypalemailid;
	}

	public void setPayPalEmailId(String paypalemailid) {	this.paypalemailid = paypalemailid;	}

	public String getPayoutId() {
		return payoutid;
	}

	public void setPayoutId(String payoutid) {	this.payoutid = payoutid;	}

	public String getIsDefault() {
		return isdefault;
	}

	public void setIsDefault(String isdefault) {	this.isdefault = isdefault;	}

	public String getPayoutMethod() {
		return payoutmethod;
	}

	public void setPayoutMethod(String payoutmethod) {	this.payoutmethod = payoutmethod;	}
}
