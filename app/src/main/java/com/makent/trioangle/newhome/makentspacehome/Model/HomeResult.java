package com.makent.trioangle.newhome.makentspacehome.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

public class HomeResult implements Serializable {

    @SerializedName("status_code")
    @Expose
    private String status_code;

    @SerializedName("success_message")
    @Expose
    private String success_message;

    public String getStatus_code() {
        return status_code;
    }

    public void setStatus_code(String status_code) {
        this.status_code = status_code;
    }

    public String getSuccess_message() {
        return success_message;
    }

    public void setSuccess_message(String success_message) {
        this.success_message = success_message;
    }

    @SerializedName("total_page")
    @Expose
    private String totalPage;

    @SerializedName("min_price")
    @Expose
    private String minprice;

    @SerializedName("max_price")
    @Expose
    private String maxprice;

    @SerializedName("data")
    @Expose
    private ArrayList<HomeListModel> homeListModels;

    public String getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(String totalPage) {
        this.totalPage = totalPage;
    }

    public String getMinprice() {
        return minprice;
    }

    public void setMinprice(String minprice) {
        this.minprice = minprice;
    }

    public String getMaxprice() {
        return maxprice;
    }

    public void setMaxprice(String maxprice) {
        this.maxprice = maxprice;
    }

    public ArrayList<HomeListModel> getHomeListModels() {
        return homeListModels;
    }

    public void setHomeListModels(ArrayList<HomeListModel> homeListModels) {
        this.homeListModels = homeListModels;
    }
}
