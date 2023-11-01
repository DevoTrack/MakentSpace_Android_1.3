
package com.makent.trioangle.newhome.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class HostExperienceModel implements Serializable
{

    @SerializedName("success_message")
    @Expose
    private String successMessage;
    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("total_page")
    @Expose
    private Integer totalPage;

    @SerializedName("Explore list")
    @Expose
    private ArrayList<ExploreList> exploreList = null;
    @SerializedName("Lists")
    @Expose
    private ArrayList<Lists> lists = null;
    private final static long serialVersionUID = 6381122331265848833L;

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<ExploreList> getExploreList() {
        return exploreList;
    }

    public void setExploreList(ArrayList<ExploreList> exploreList) {
        this.exploreList = exploreList;
    }

    public ArrayList<Lists> getLists() {
        return lists;
    }

    public void setLists(ArrayList<Lists> lists) {
        this.lists = lists;
    }

    public Integer getTotalPage() {
        return totalPage;
    }

    public void setTotalPage(Integer totalPage) {
        this.totalPage = totalPage;
    }
}
