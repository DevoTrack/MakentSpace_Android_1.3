package com.makent.trioangle.model;

/**
 *
 * @package     com.makent.trioangle
 * @subpackage  model
 * @category    Mapmodel
 * @author      Trioangle Product Team
 * @version     1.1
 */

import java.io.Serializable;

public class Mapmodel implements Serializable {
	private String roomimage,roomid,roomtype,roomname,roomprice,roomrating,roomreview,roomiswishlist,
			roomcountryname,currencycode,currencysymbol,roomlat,roomlong,roomselected;
	public String type;


	public Mapmodel() {

	}
	public Mapmodel(String type) {
		this.type = type;
	}

	public Mapmodel(String type,String roomimage, String roomid, String roomname, String roomprice, String roomrating,
					String roomreview,String roomiswishlist, String roomcountryname, String currencycode, String currencysymbol,
					String roomtype, String roomlat, String roomlong, String roomselected) {
		this.type=type;
		this.roomimage=roomimage;
		this.roomid=roomid;
		this.roomname=roomname;
		this.roomprice=roomprice;
		this.roomrating=roomrating;
		this.roomreview=roomreview;
		this.roomiswishlist=roomiswishlist;
		this.roomcountryname=roomcountryname;
		this.currencycode=currencycode;
		this.currencysymbol=currencysymbol;

		this.roomtype=roomtype;
		this.roomlat=roomlat;
		this.roomlong=roomlong;
		this.roomselected=roomselected;

	}
	 //Detailed list space

	public String getRoomImage() {	return roomimage; }

	public void setRoomImage(String roomimage) {this.roomimage = roomimage; }

	public String getRoomid() {	return roomid;	}

	public void setRoomid(String roomid) {this.roomid = roomid; }

	public String getRoomname() {return roomname;}

	public void setRoomname(String roomname) {this.roomname = roomname; }

	public String getRoomprice() {return roomprice;	}

	public void setRoomprice(String roomprice) {this.roomprice = roomprice; }

	public String getRoomrating() { return roomrating;	}

	public void setRoomrating(String roomrating) {this.roomrating = roomrating; }

	public String getRoomreview() { return roomreview;	}

	public void setRoomreview(String roomreview) {this.roomreview = roomreview; }

	public String getRoomiswishlist() { return roomiswishlist;	}

	public void setRoomiswishlist(String roomiswishlist) {this.roomiswishlist = roomiswishlist; }

	public String getRoomcountryname() { return roomcountryname;	}

	public void setRoomcountryname(String roomcountryname) {this.roomcountryname = roomcountryname; }

	public String getCurrencycode() { return currencycode;	}

	public void setCurrencycode(String currencycode) {this.currencycode = currencycode; }

	public String getCurrencysymbol() { return currencysymbol;	}

	public void setCurrencysymbol(String currencysymbol) {this.currencysymbol = currencysymbol; }

	public String getRoomtype() {
		return roomtype;
	}

	public void setRoomtype(String roomtype) {this.roomtype = roomtype;	}

	public String getRoomlat() {
		return roomlat;
	}

	public void setRoomlat(String roomlat) {this.roomlat = roomlat;	}

	public String getRoomlong() {
		return roomlong;
	}

	public void setRoomlong(String roomlong) {	this.roomlong = roomlong; }

	public String getRoomSelected() {
		return roomselected;
	}

	public void setRoomSelected(String roomselected) {	this.roomselected = roomselected;
		System.out.println(" ****** Room Seleted *******"+roomselected); }
}
