package com.makent.trioangle.model.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*****************************************************************
 Inbox Result Model Class
 ****************************************************************/


public class InboxResult implements Serializable {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("unread_count")
    @Expose
    private String unreadMessageCount;

    @SerializedName("data")
    @Expose
    private ArrayList<InboxDataModel> data;

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

    public String getUnreadMessageCount() {
        return unreadMessageCount;
    }

    public void setUnreadMessageCount(String unreadMessageCount) {
        this.unreadMessageCount = unreadMessageCount;
    }

    public ArrayList<InboxDataModel> getData() {
        return data;
    }

    public void setData(ArrayList<InboxDataModel> data) {
        this.data = data;
    }
}
