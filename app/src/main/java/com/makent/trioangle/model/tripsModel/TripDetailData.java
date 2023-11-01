package com.makent.trioangle.model.tripsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/*****************************************************************
 Trip Detail Data Model Class
 ****************************************************************/


public class TripDetailData implements Serializable{

    @SerializedName("reservation_id")
    @Expose
    private String reservationId;

    @SerializedName("space_id")
    @Expose
    private String spaceId;

    @SerializedName("guest_user_name")
    @Expose
    private String userName;

    @SerializedName("guest_thumb_image")
    @Expose
    private String guestThumbImage;

    @SerializedName("reservation_status")
    @Expose
    private String reservationStatus;

    @SerializedName("booking_status")
    @Expose
    private String bookingStatus;


    @SerializedName("checkin")
    @Expose
    private String checkIn;

    @SerializedName("reservation_date")
    @Expose
    private String reservationDate;

    @SerializedName("checkout")
    @Expose
    private String checkOut;

    @SerializedName("space_name")
    @Expose
    private String  spaceName;

    @SerializedName("space_location")
    @Expose
    private String spaceLocation;

    @SerializedName("space_type")
    @Expose
    private String spaceType;

    @SerializedName("total_nights")
    @Expose
    private String totalNights;

    @SerializedName("number_of_guests")
    @Expose
    private String guestCount;

    @SerializedName("host_user_name")
    @Expose
    private String hostUserName;

    @SerializedName("host_thumb_image")
    @Expose
    private String hostThumbImage;

    @SerializedName("space_image")
    @Expose
    private String spaceImage;

    @SerializedName("total_cost")
    @Expose
    private String totalCost;

    @SerializedName("total_trips_count")
    @Expose
    private String totalTripsCount;

    @SerializedName("host_user_id")
    @Expose
    private String hostUserId;

    @SerializedName("can_view_receipt")
    @Expose
    private String canViewReceipt;


    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;


    @SerializedName("start_time")
    @Expose
    private String start_time;

    @SerializedName("end_time")
    @Expose
    private String end_time;

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }


    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getSpaceId() {
        return spaceId;
    }

    public void setSpaceId(String spaceId) {
        this.spaceId = spaceId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getGuestThumbImage() {
        return guestThumbImage;
    }

    public void setGuestThumbImage(String guestThumbImage) {
        this.guestThumbImage = guestThumbImage;
    }

    public String getReservationStatus() {
        return reservationStatus;
    }

    public void setReservationStatus(String reservationStatus) {
        this.reservationStatus = reservationStatus;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(String checkIn) {
        this.checkIn = checkIn;
    }

    public String getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(String checkOut) {
        this.checkOut = checkOut;
    }

    public String getSpaceName() {
        return spaceName;
    }

    public void setSpaceName(String spaceName) {
        this.spaceName = spaceName;
    }

    public String getSpaceLocation() {
        return spaceLocation;
    }

    public void setSpaceLocation(String spaceLocation) {
        this.spaceLocation = spaceLocation;
    }

    public String getSpaceType() {
        return spaceType;
    }

    public void setSpaceType(String spaceType) {
        this.spaceType = spaceType;
    }

    public String getTotalNights() {
        return totalNights;
    }

    public void setTotalNights(String totalNights) {
        this.totalNights = totalNights;
    }

    public String getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(String guestCount) {
        this.guestCount = guestCount;
    }

    public String getHostUserName() {
        return hostUserName;
    }

    public void setHostUserName(String hostUserName) {
        this.hostUserName = hostUserName;
    }

    public String getHostThumbImage() {
        return hostThumbImage;
    }

    public void setHostThumbImage(String hostThumbImage) {
        this.hostThumbImage = hostThumbImage;
    }

    public String getSpaceImage() {
        return spaceImage;
    }

    public void setSpaceImage(String spaceImage) {
        this.spaceImage = spaceImage;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getTotalTripsCount() {
        return totalTripsCount;
    }

    public void setTotalTripsCount(String totalTripsCount) {
        this.totalTripsCount = totalTripsCount;
    }

    public String getHostUserId() {
        return hostUserId;
    }

    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }



    public String getCanViewReceipt() {
        return canViewReceipt;
    }

    public void setCanViewReceipt(String canViewReceipt) {
        this.canViewReceipt = canViewReceipt;
    }




    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }


}
