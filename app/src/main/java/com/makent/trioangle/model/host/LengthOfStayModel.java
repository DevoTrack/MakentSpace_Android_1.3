package com.makent.trioangle.model.host;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by trioangle on 8/9/18.
 */

public class LengthOfStayModel implements Serializable {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;


    @SerializedName("price_rules")
    @Expose
    private ArrayList<LengthOfStayArrayModel> priceRules;

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

    public ArrayList<LengthOfStayArrayModel> getPriceRules() {
        return priceRules;
    }

    public void setPriceRules(ArrayList<LengthOfStayArrayModel> priceRules) {
        this.priceRules = priceRules;
    }
}
