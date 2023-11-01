package com.makent.trioangle.model.host;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by trioangle on 8/3/18.
 */

public class ReservationDetailsModel {

    @SerializedName("reservation_id")
    @Expose
    private String reservationId;

    @SerializedName("space_id")
    @Expose
    private String spaceID;

    @SerializedName("host_user_deleted")
    @Expose
    private boolean isDeletedUser;


    @SerializedName("reservation_status")
    @Expose
    private String bookingStatus;

    @SerializedName("other_user_id")
    @Expose
    private String otherUserId;

    @SerializedName("other_user_name")
    @Expose
    private String otherUserName;

    @SerializedName("other_thumb_image")
    @Expose
    private String otherThumbImage;

    @SerializedName("other_user_location")
    @Expose
    private String otherUserLocation;

    @SerializedName("member_from")
    @Expose
    private String memberFrom;

    @SerializedName("reservation_date")
    @Expose
    private String reservationDate;

    @SerializedName("space_name")
    @Expose
    private String spaceName;


    @SerializedName("space_location")
    @Expose
    private String spaceLocation;

    @SerializedName("guest_count")
    @Expose
    private String guestCount;

    @SerializedName("space_image")
    @Expose
    private String spaceImage;

    @SerializedName("checkin")
    @Expose
    private String checkIn;

    @SerializedName("checkout")
    @Expose
    private String checkOut;

    @SerializedName("total_cost")
    @Expose
    private String totalCost;

    @SerializedName("start_time")
    @Expose
    private String startTime;

    @SerializedName("end_time")
    @Expose
    private String endTime;

    @SerializedName("per_night_price")
    @Expose
    private String perNightPrice;

    @SerializedName("length_of_stay_type")
    @Expose
    private String lengthOfStayType;

    @SerializedName("length_of_stay_discount")
    @Expose
    private String lengthOfStayDiscount;

    @SerializedName("length_of_stay_discount_price")
    @Expose
    private String lengthOfStayDiscountPrice;


    @SerializedName("security")
    @Expose
    private String securityDeposit;




    @SerializedName("host_fee")
    @Expose
    private String hostFee;

    @SerializedName("host_penalty_amount")
    @Expose
    private String hostPenaltyAmount;


    @SerializedName("can_view_receipt")
    @Expose
    private String canViewReceipt;

    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;

    @SerializedName("expire_timer")
    @Expose
    private String expireTimer;

    @SerializedName("payment_recieved_date")
    @Expose
    private String paymentRecievedDate;

    public boolean isDeletedUser() {
        return isDeletedUser;
    }

    public void setDeletedUser(boolean deletedUser) {
        isDeletedUser = deletedUser;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getOtherUserName() {
        return otherUserName;
    }

    public void setOtherUserName(String otherUserName) {
        this.otherUserName = otherUserName;
    }

    public String getOtherThumbImage() {
        return otherThumbImage;
    }

    public void setOtherThumbImage(String otherThumbImage) {
        this.otherThumbImage = otherThumbImage;
    }

    public String getOtherUserLocation() {
        return otherUserLocation;
    }

    public void setOtherUserLocation(String otherUserLocation) {
        this.otherUserLocation = otherUserLocation;
    }

    public String getMemberFrom() {
        return memberFrom;
    }

    public void setMemberFrom(String memberFrom) {
        this.memberFrom = memberFrom;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
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

    public String getGuestCount() {
        return guestCount;
    }

    public void setGuestCount(String guestCount) {
        this.guestCount = guestCount;
    }

    public String getSpaceImage() {
        return spaceImage;
    }

    public void setSpaceImage(String spaceImage) {
        this.spaceImage = spaceImage;
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

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getPerNightPrice() {
        return perNightPrice;
    }

    public void setPerNightPrice(String perNightPrice) {
        this.perNightPrice = perNightPrice;
    }

    public String getLengthOfStayType() {
        return lengthOfStayType;
    }

    public void setLengthOfStayType(String lengthOfStayType) {
        this.lengthOfStayType = lengthOfStayType;
    }

    public String getLengthOfStayDiscount() {
        return lengthOfStayDiscount;
    }

    public void setLengthOfStayDiscount(String lengthOfStayDiscount) {
        this.lengthOfStayDiscount = lengthOfStayDiscount;
    }

    public String getLengthOfStayDiscountPrice() {
        return lengthOfStayDiscountPrice;
    }

    public void setLengthOfStayDiscountPrice(String lengthOfStayDiscountPrice) {
        this.lengthOfStayDiscountPrice = lengthOfStayDiscountPrice;
    }

    public String getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(String securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getHostFee() {
        return hostFee;
    }

    public void setHostFee(String hostFee) {
        this.hostFee = hostFee;
    }

    public String getHostPenaltyAmount() {
        return hostPenaltyAmount;
    }

    public void setHostPenaltyAmount(String hostPenaltyAmount) {
        this.hostPenaltyAmount = hostPenaltyAmount;
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

    public String getExpireTimer() {
        return expireTimer;
    }

    public void setExpireTimer(String expireTimer) {
        this.expireTimer = expireTimer;
    }

    public String getReservationId() {
        return reservationId;
    }

    public void setReservationId(String reservationId) {
        this.reservationId = reservationId;
    }

    public String getSpaceID() {
        return spaceID;
    }

    public void setSpaceID(String spaceID) {
        this.spaceID = spaceID;
    }
}
