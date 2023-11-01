package com.makent.trioangle.model.explore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;


/*****************************************************************
 Explore Result Model Class
 ****************************************************************/


public class ExploreResult implements Serializable {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("total_page")
    @Expose
    private String totalPage;

    @SerializedName("min_price")
    @Expose
    private String minPrice;

    @SerializedName("max_price")
    @Expose
    private String maxPrice;

    @SerializedName("data")
    @Expose
    private ArrayList<ExploreDataModel> exploreDataModels;

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

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(String minPrice) {
        this.minPrice = minPrice;
    }

    public String getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(String maxPrice) {
        this.maxPrice = maxPrice;
    }

    public ArrayList<ExploreDataModel> getExploreDataModels() {
        return exploreDataModels;
    }

    public void setExploreDataModels(ArrayList<ExploreDataModel> exploreDataModels) {
        this.exploreDataModels = exploreDataModels;
    }
}
