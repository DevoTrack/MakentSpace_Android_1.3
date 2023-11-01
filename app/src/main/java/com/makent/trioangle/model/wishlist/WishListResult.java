package com.makent.trioangle.model.wishlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*****************************************************************
 Wishlist Result Model Class
 ****************************************************************/


public class WishListResult implements Serializable {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("wishlist_data")
    @Expose
    private ArrayList <WishListData> wishListData;

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

    public ArrayList<WishListData> getWishListData() {
        return wishListData;
    }

    public void setWishListData(ArrayList<WishListData> wishListData) {
        this.wishListData = wishListData;
    }
}
