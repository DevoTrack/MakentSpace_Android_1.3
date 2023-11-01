package com.makent.trioangle.model.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/*****************************************************************
 Chat Detail Model Class
 ****************************************************************/

public class ChatDetail {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("date/time")
    @Expose
    private String dateTime;

    @SerializedName("inquiry_title")
    @Expose
    private String inquiryTitle;



    @SerializedName("date")
    @Expose
    private String date;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getInquiryTitle() {
        return inquiryTitle;
    }

    public void setInquiryTitle(String inquiryTitle) {
        this.inquiryTitle = inquiryTitle;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
