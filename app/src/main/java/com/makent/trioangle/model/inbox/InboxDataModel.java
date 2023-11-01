package com.makent.trioangle.model.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 2/8/18.
 */

public class InboxDataModel implements Serializable {

    @SerializedName("booking_status")
    @Expose
    private String bookingStatus;

    @SerializedName("reservation_id")
    @Expose
    private String reservationId;

    @SerializedName("space_id")
    @Expose
    private String spaceId;

    @SerializedName("is_message_read")
    @Expose
    private String isMessageRead;

    @SerializedName("reservation_status")
    @Expose
    private String messageStatus;

    @SerializedName("other_user_id")
    @Expose
    private String otherUserId;

    @SerializedName("request_user_id")
    @Expose
    private String requestUserId;

    @SerializedName("other_user_name")
    @Expose
    private String otherUserName;

    @SerializedName("other_thumb_image")
    @Expose
    private String otherThumbImage;


    @SerializedName("space_name")
    @Expose
    private String spaceName;

    @SerializedName("space_location")
    @Expose
    private String spaceLocation;

    @SerializedName("last_message")
    @Expose
    private String lastMessage;

    @SerializedName("checkin")
    @Expose
    private String checkInTime;

    @SerializedName("checkout")
    @Expose
    private String checkOutTime;


    @SerializedName("reservation_date")
    @Expose
    private String reservationDate;

    @SerializedName("total_cost")
    @Expose
    private String totalCost;

    @SerializedName("host_fee")
    @Expose
    private String hostFee;

    @SerializedName("coupon_amount")
    @Expose
    private String couponAmount;

    @SerializedName("host_penalty_amount")
    @Expose
    private String hostPenaltyAmount;

    @SerializedName("service_fee")
    @Expose
    private String serviceFee;

    @SerializedName("security")
    @Expose
    private String securityDeposit;

    @SerializedName("host_user_id")
    @Expose
    private String hostUserId;

    @SerializedName("space_image")
    @Expose
    private String spaceImage;

    @SerializedName("review_count")
    @Expose
    private String reviewCount;

    @SerializedName("expire_timer")
    @Expose
    private String expireTimer;

    @SerializedName("special_offer_id")
    @Expose
    private String specialOfferId;

    @SerializedName("special_offer_status")
    @Expose
    private String specialOfferStatus;

    @SerializedName("host_user_deleted")
    @Expose
    private boolean isDeletedUser;


    public boolean isDeletedUser() {
        return isDeletedUser;
    }

    public void setDeletedUser(boolean deletedUser) {
        isDeletedUser = deletedUser;
    }

    public String getReservationDate() {
        return reservationDate;
    }

    public void setReservationDate(String reservationDate) {
        this.reservationDate = reservationDate;
    }

    public String getOtherUserId() {
        return otherUserId;
    }

    public String getRequestUserId() {
        return requestUserId;
    }

    public void setRequestUserId(String requestUserId) {
        this.requestUserId = requestUserId;
    }

    public void setOtherUserId(String otherUserId) {
        this.otherUserId = otherUserId;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
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

    public String getIsMessageRead() {
        return isMessageRead;
    }

    public void setIsMessageRead(String isMessageRead) {
        this.isMessageRead = isMessageRead;
    }

    public String getMessageStatus() {
        return messageStatus;
    }

    public void setMessageStatus(String messageStatus) {
        this.messageStatus = messageStatus;
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

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getCheckInTime() {
        return checkInTime;
    }

    public void setCheckInTime(String checkInTime) {
        this.checkInTime = checkInTime;
    }

    public String getCheckOutTime() {
        return checkOutTime;
    }

    public void setCheckOutTime(String checkOutTime) {
        this.checkOutTime = checkOutTime;
    }

    public String getTotalCost() {
        return totalCost;
    }

    public void setTotalCost(String totalCost) {
        this.totalCost = totalCost;
    }

    public String getHostFee() {
        return hostFee;
    }

    public void setHostFee(String hostFee) {
        this.hostFee = hostFee;
    }

    public String getCouponAmount() {
        return couponAmount;
    }

    public void setCouponAmount(String couponAmount) {
        this.couponAmount = couponAmount;
    }

    public String getHostPenaltyAmount() {
        return hostPenaltyAmount;
    }

    public void setHostPenaltyAmount(String hostPenaltyAmount) {
        this.hostPenaltyAmount = hostPenaltyAmount;
    }

    public String getServiceFee() {
        return serviceFee;
    }

    public void setServiceFee(String serviceFee) {
        this.serviceFee = serviceFee;
    }

    public String getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(String securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getHostUserId() {
        return hostUserId;
    }

    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }

    public String getSpaceImage() {
        return spaceImage;
    }

    public void setSpaceImage(String spaceImage) {
        this.spaceImage = spaceImage;
    }

    public String getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(String reviewCount) {
        this.reviewCount = reviewCount;
    }

    public String getExpireTimer() {
        return expireTimer;
    }

    public void setExpireTimer(String expireTimer) {
        this.expireTimer = expireTimer;
    }

    public String getSpecialOfferId() {
        return specialOfferId;
    }

    public void setSpecialOfferId(String specialOfferId) {
        this.specialOfferId = specialOfferId;
    }

    public String getSpecialOfferStatus() {
        return specialOfferStatus;
    }

    public void setSpecialOfferStatus(String specialOfferStatus) {
        this.specialOfferStatus = specialOfferStatus;
    }




}
