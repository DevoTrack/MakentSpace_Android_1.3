package com.makent.trioangle.model.payout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by trioangle on 3/8/18.
 */

public class PayoutDetailResult {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("payout_details")
    @Expose
    private ArrayList<PayoutDetail> payout_details;

    @SerializedName("stripe_publish_key")
    @Expose
    private String stripe_publish_key;

    @SerializedName("user_details")
    @Expose
    private UserPayoutDetails userPayoutDetails;

    public String getStripe_publish_key() {
        return stripe_publish_key;
    }

    public void setStripe_publish_key(String stripe_publish_key) {
        this.stripe_publish_key = stripe_publish_key;
    }

    public UserPayoutDetails getUserPayoutDetails() {
        return userPayoutDetails;
    }

    public void setUserPayoutDetails(UserPayoutDetails userPayoutDetails) {
        this.userPayoutDetails = userPayoutDetails;
    }

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

    public ArrayList<PayoutDetail> getPayout_details() {
        return payout_details;
    }

    public void setPayout_details(ArrayList<PayoutDetail> payout_details) {
        this.payout_details = payout_details;
    }
}
