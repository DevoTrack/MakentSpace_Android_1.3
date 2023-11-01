package com.makent.trioangle.model.host;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by trioangle on 8/3/18.
 */

public class HostCalendarListModel implements Serializable {

    @SerializedName("room_id")
    @Expose
    private String roomId;

    @SerializedName("room_price")
    @Expose
    private String roomPrice;

    @SerializedName("nightly_price")
    @Expose
    private ArrayList<String> nightlyPrice;

    @SerializedName("room_type")
    @Expose
    private String roomType;

    @SerializedName("room_name")
    @Expose
    private String roomName;

    @SerializedName("room_thumb_images")
    @Expose
    private String[] roomThumbImages;

    @SerializedName("remaining_steps")
    @Expose
    private String remainingSteps;

    @SerializedName("room_location")
    @Expose
    private String roomLocation;

    @SerializedName("blocked_dates")
    @Expose
    private String[] blockedDates;

    @SerializedName("reserved_dates")
    @Expose
    private String[] reservedDates;

    @SerializedName("room_currency_symbol")
    @Expose
    private String roomCurrencySymbol;

    @SerializedName("room_currency_code")
    @Expose
    private String roomCurrencyCode;


    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public ArrayList<String> getNightlyPrice() {
        return nightlyPrice;
    }

    public void setNightlyPrice(ArrayList<String> nightlyPrice) {
        this.nightlyPrice = nightlyPrice;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String[] getRoomThumbImages() {
        return roomThumbImages;
    }

    public void setRoomThumbImages(String[] roomThumbImages) {
        this.roomThumbImages = roomThumbImages;
    }

    public String getRemainingSteps() {
        return remainingSteps;
    }

    public void setRemainingSteps(String remainingSteps) {
        this.remainingSteps = remainingSteps;
    }

    public String getRoomLocation() {
        return roomLocation;
    }

    public void setRoomLocation(String roomLocation) {
        this.roomLocation = roomLocation;
    }

    public String[] getBlockedDates() {
        return blockedDates;
    }

    public void setBlockedDates(String blockedDates[]) {
        this.blockedDates = blockedDates;
    }

    public String[] getReservedDates() {
        return reservedDates;
    }

    public void setReservedDates(String[] reservedDates) {
        this.reservedDates = reservedDates;
    }

    public String getRoomCurrencySymbol() {
        return roomCurrencySymbol;
    }

    public void setRoomCurrencySymbol(String roomCurrencySymbol) {
        this.roomCurrencySymbol = roomCurrencySymbol;
    }

    public String getRoomCurrencyCode() {
        return roomCurrencyCode;
    }

    public void setRoomCurrencyCode(String roomCurrencyCode) {
        this.roomCurrencyCode = roomCurrencyCode;
    }
}
