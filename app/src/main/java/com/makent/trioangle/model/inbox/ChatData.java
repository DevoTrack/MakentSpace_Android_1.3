package com.makent.trioangle.model.inbox;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


/*****************************************************************
 Chat Data Model Class
 ****************************************************************/


public class ChatData  {

    @SerializedName("sender_user_name")
    @Expose
    private String senderUserName;

    @SerializedName("sender_thumb_image")
    @Expose
    private String senderThumbImage;

    @SerializedName("sender_message_status")
    @Expose
    private String senderMessageStatus;

    @SerializedName("sender_details")
    @Expose
    private ChatDetail senderDetails;

    @SerializedName("sender_messages")
    @Expose
    private String senderMessages;

    @SerializedName("receiver_user_name")
    @Expose
    private String receiverUserName;

    @SerializedName("receiver_thumb_image")
    @Expose
    private String receiverThumbImage;

    @SerializedName("receiver_message_status")
    @Expose
    private String receiverMessageStatus;

    @SerializedName("receiver_messages")
    @Expose
    private String receiverMessages;

    @SerializedName("conversation_time")
    @Expose
    private String conversationTime;

    @SerializedName("receiver_details")
    @Expose
    private ChatDetail receiverDetails;

    private int type;

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

    public String getReceiverMessageStatus() {
        return receiverMessageStatus;
    }

    public void setReceiverMessageStatus(String receiverMessageStatus) {
        this.receiverMessageStatus = receiverMessageStatus;
    }

    public ChatDetail getReceiverDetails() {
        return receiverDetails;
    }

    public void setReceiverDetails(ChatDetail receiverDetails) {
        this.receiverDetails = receiverDetails;
    }

    public String getReceiverMessages() {
        return receiverMessages;
    }

    public void setReceiverMessages(String receiverMessages) {
        this.receiverMessages = receiverMessages;
    }

    public String getSenderMessageStatus() {
        return senderMessageStatus;
    }

    public void setSenderMessageStatus(String senderMessageStatus) {
        this.senderMessageStatus = senderMessageStatus;
    }

    public ChatDetail getSenderDetails() {
        return senderDetails;
    }

    public void setSenderDetails(ChatDetail senderDetails) {
        this.senderDetails = senderDetails;
    }


    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getSenderMessages() {
        return senderMessages;
    }

    public void setSenderMessages(String senderMessages) {
        this.senderMessages = senderMessages;
    }

    public String getConversationTime() {
        return conversationTime;
    }

    public void setConversationTime(String conversationTime) {
        this.conversationTime = conversationTime;
    }
}
