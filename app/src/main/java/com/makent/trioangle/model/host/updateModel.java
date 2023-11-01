package com.makent.trioangle.model.host;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by trioangle on 8/3/18.
 */

public class updateModel implements Serializable {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("blocked_dates")
    @Expose
    private String[] blockedDates;

    @SerializedName("reserved_dates")
    @Expose
    private String[] reservedDates;


    @SerializedName("nightly_price")
    @Expose
    private ArrayList<String> nightlyPrice;


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

    public String[] getBlockedDates() {
        return blockedDates;
    }

    public void setBlockedDates(String[] blockedDates) {
        this.blockedDates = blockedDates;
    }

    public String[] getReservedDates() {
        return reservedDates;
    }

    public void setReservedDates(String[] reservedDates) {
        this.reservedDates = reservedDates;
    }

    public ArrayList<String> getNightlyPrice() {
        return nightlyPrice;
    }

    public void setNightlyPrice(ArrayList<String> nightlyPrice) {
        this.nightlyPrice = nightlyPrice;
    }
}
