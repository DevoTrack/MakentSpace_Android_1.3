package com.makent.trioangle.newhome.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ExperienceCategoryModel {
    @SerializedName("success_message")
    @Expose
    private String successMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("host_experience_categories")
    @Expose
    private ArrayList<ExperienceCategoryList> ExploreList=new ArrayList<>();

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

    public ArrayList<ExperienceCategoryList> getExploreList() {
        return ExploreList;
    }

    public void setExploreList(ArrayList<ExperienceCategoryList> exploreList) {
        ExploreList = exploreList;
    }
}
