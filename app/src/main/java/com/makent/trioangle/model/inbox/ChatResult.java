package com.makent.trioangle.model.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/*****************************************************************
 Chat Result Model Class
 ****************************************************************/

public class ChatResult {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("sender_user_name")
    @Expose
    private String senderUserName;

    @SerializedName("sender_thumb_image")
    @Expose
    private String senderThumbImage;

    @SerializedName("receiver_user_name")
    @Expose
    private String receiverUserName;

    @SerializedName("receiver_thumb_image")
    @Expose
    private String receiverThumbImage;

    @SerializedName("data")
    @Expose
    private ArrayList<ChatData> data;

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

    public String getSenderUserName() {
        return senderUserName;
    }

    public void setSenderUserName(String senderUserName) {
        this.senderUserName = senderUserName;
    }

    public String getSenderThumbImage() {
        return senderThumbImage;
    }

    public void setSenderThumbImage(String senderThumbImage) {
        this.senderThumbImage = senderThumbImage;
    }

    public String getReceiverUserName() {
        return receiverUserName;
    }

    public void setReceiverUserName(String receiverUserName) {
        this.receiverUserName = receiverUserName;
    }

    public String getReceiverThumbImage() {
        return receiverThumbImage;
    }

    public void setReceiverThumbImage(String receiverThumbImage) {
        this.receiverThumbImage = receiverThumbImage;
    }

    public ArrayList<ChatData> getData() {
        return data;
    }

    public void setData(ArrayList<ChatData> data) {
        this.data = data;
    }
}
