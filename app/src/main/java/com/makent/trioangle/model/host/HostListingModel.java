package com.makent.trioangle.model.host;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by trioangle on 8/6/18.
 */

public class HostListingModel implements Serializable {


    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;


    @SerializedName("total_page")
    @Expose
    private String totalPage;

    @SerializedName("listed")
    @Expose
    private ArrayList<HostListedModel> listed;


    @SerializedName("unlisted")
    @Expose
    private ArrayList<HostListedModel> unlisted;




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

    public ArrayList<HostListedModel> getListed() {
        return listed;
    }

    public void setListed(ArrayList<HostListedModel> listed) {
        this.listed = listed;
    }

    public ArrayList<HostListedModel> getUnlisted() {
        return unlisted;
    }

    public void setUnlisted(ArrayList<HostListedModel> unlisted) {
        this.unlisted = unlisted;
    }
}
