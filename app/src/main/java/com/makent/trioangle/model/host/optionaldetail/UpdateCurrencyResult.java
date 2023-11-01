package com.makent.trioangle.model.host.optionaldetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 20/8/18.
 */

public class UpdateCurrencyResult implements Serializable {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("room_price")
    @Expose
    private String roomPrice;

    @SerializedName("weekly_price")
    @Expose
    private String weeklyPrice;

    @SerializedName("monthly_price")
    @Expose
    private String monthlyPrice;

    @SerializedName("cleaning_fee")
    @Expose
    private String cleaningFee;

    @SerializedName("additional_guests_fee")
    @Expose
    private String additionalGuestsFee;

    @SerializedName("security_deposit")
    @Expose
    private String securityDeposit;

    @SerializedName("weekend_pricing")
    @Expose
    private String weekendPricing;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getWeeklyPrice() {
        return weeklyPrice;
    }

    public void setWeeklyPrice(String weeklyPrice) {
        this.weeklyPrice = weeklyPrice;
    }

    public String getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(String monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public String getCleaningFee() {
        return cleaningFee;
    }

    public void setCleaningFee(String cleaningFee) {
        this.cleaningFee = cleaningFee;
    }

    public String getAdditionalGuestsFee() {
        return additionalGuestsFee;
    }

    public void setAdditionalGuestsFee(String additionalGuestsFee) {
        this.additionalGuestsFee = additionalGuestsFee;
    }

    public String getSecurityDeposit() {
        return securityDeposit;
    }

    public void setSecurityDeposit(String securityDeposit) {
        this.securityDeposit = securityDeposit;
    }

    public String getWeekendPricing() {
        return weekendPricing;
    }

    public void setWeekendPricing(String weekendPricing) {
        this.weekendPricing = weekendPricing;
    }
}
