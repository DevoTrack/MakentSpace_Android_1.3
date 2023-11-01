package com.makent.trioangle.model.payout;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by trioangle on 3/8/18.
 */

public class PayoutDetail {

    @SerializedName("payout_id")
    @Expose
    private String payoutId;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("payout_method")
    @Expose
    private String payoutMethod;

    @SerializedName("paypal_email")
    @Expose
    private String paypalEmail;

    @SerializedName("set_default")
    @Expose
    private String isDefault;

    public String getPayoutId() {
        return payoutId;
    }

    public void setPayoutId(String payoutId) {
        this.payoutId = payoutId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPayoutMethod() {
        return payoutMethod;
    }

    public void setPayoutMethod(String payoutMethod) {
        this.payoutMethod = payoutMethod;
    }

    public String getPaypalEmail() {
        return paypalEmail;
    }

    public void setPaypalEmail(String paypalEmail) {
        this.paypalEmail = paypalEmail;
    }


    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }
}
