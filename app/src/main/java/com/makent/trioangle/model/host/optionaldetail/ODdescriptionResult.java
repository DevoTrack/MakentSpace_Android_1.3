package com.makent.trioangle.model.host.optionaldetail;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by trioangle on 9/8/18.
 */

public class ODdescriptionResult {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("space_msg")
    @Expose
    private String spaceMsg;

    @SerializedName("guest_access_msg")
    @Expose
    private String guestAccessMsg;

    @SerializedName("interaction_with_guest_msg")
    @Expose
    private String interactionWithGuestMsg;

    @SerializedName("overview_msg")
    @Expose
    private String overviewMsg;

    @SerializedName("getting_arround_msg")
    @Expose
    private String gettingArroundMsg;

    @SerializedName("other_things_to_note_msg")
    @Expose
    private String otherThingsToNoteMsg;

    @SerializedName("house_rules_msg")
    @Expose
    private String houseRulesMsg;

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

    public String getSpaceMsg() {
        return spaceMsg;
    }

    public void setSpaceMsg(String spaceMsg) {
        this.spaceMsg = spaceMsg;
    }

    public String getGuestAccessMsg() {
        return guestAccessMsg;
    }

    public void setGuestAccessMsg(String guestAccessMsg) {
        this.guestAccessMsg = guestAccessMsg;
    }

    public String getInteractionWithGuestMsg() {
        return interactionWithGuestMsg;
    }

    public void setInteractionWithGuestMsg(String interaction_with_guest_msg) {
        this.interactionWithGuestMsg = interaction_with_guest_msg;
    }

    public String getOverviewMsg() {
        return overviewMsg;
    }

    public void setOverviewMsg(String overview_msg) {
        this.overviewMsg = overview_msg;
    }

    public String getGettingArroundMsg() {
        return gettingArroundMsg;
    }

    public void setGettingArroundMsg(String gettingArroundMsg) {
        this.gettingArroundMsg = gettingArroundMsg;
    }

    public String getOtherThingsToNoteMsg() {
        return otherThingsToNoteMsg;
    }

    public void setOtherThingsToNoteMsg(String otherThingsToNoteMsg) {
        this.otherThingsToNoteMsg = otherThingsToNoteMsg;
    }

    public String getHouseRulesMsg() {
        return houseRulesMsg;
    }

    public void setHouseRulesMsg(String houseRulesMsg) {
        this.houseRulesMsg = houseRulesMsg;
    }
}
