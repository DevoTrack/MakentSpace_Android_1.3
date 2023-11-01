package com.makent.trioangle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makent.trioangle.model.ExploreExpModel;

import java.io.Serializable;
import java.util.ArrayList;

public class ExploreExpResult implements Serializable {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("total_page")
    @Expose
    private String totalPage;

    @SerializedName("data")
    @Expose
    private ArrayList<ExploreExpModel> exploreExpDataModels;

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

    public ArrayList<ExploreExpModel> getExploreExpDataModels() {
        return exploreExpDataModels;
    }

    public void setExploreExpDataModels(ArrayList<ExploreExpModel> exploreExpDataModels) {
        this.exploreExpDataModels = exploreExpDataModels;
    }
}
