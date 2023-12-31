package com.makent.trioangle.model.settings;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by trioangle on 3/8/18.
 */

public class CurrencyResult  {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("currency_list")
    @Expose
    private ArrayList<CurrencyListModel> currencyList;

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

    public ArrayList<CurrencyListModel> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(ArrayList<CurrencyListModel> currencyList) {
        this.currencyList = currencyList;
    }
}
