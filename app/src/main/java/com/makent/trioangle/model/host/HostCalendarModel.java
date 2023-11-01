package com.makent.trioangle.model.host;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by trioangle on 8/3/18.
 */

public class HostCalendarModel implements Serializable {




    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("data")
    @Expose
    private ArrayList<HostCalendarListModel> calendarListModels;


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

    public ArrayList<HostCalendarListModel> getCalendarListModels() {
        return calendarListModels;
    }

    public void setCalendarListModels(ArrayList<HostCalendarListModel> calendarListModels) {
        this.calendarListModels = calendarListModels;
    }
}
