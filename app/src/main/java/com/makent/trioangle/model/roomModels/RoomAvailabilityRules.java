package com.makent.trioangle.model.roomModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 1/8/18.
 */

public class RoomAvailabilityRules implements Serializable{


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("room_id")
    @Expose
    private String roomId;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("minimum_stay")
    @Expose
    private String minimumStay;

    @SerializedName("maximum_stay")
    @Expose
    private String maximumStay;

    @SerializedName("start_date")
    @Expose
    private String startDate;

    @SerializedName("end_date")
    @Expose
    private String endDate;

    @SerializedName("during")
    @Expose
    private String during;

    @SerializedName("start_date_formatted")
    @Expose
    private String startDateFormatted;

    @SerializedName("end_date_formatted")
    @Expose
    private String endDateFormatted;



    public RoomAvailabilityRules(String id, String roomId, String type, String minimumStay, String maximumStay, String startDate, String endDate, String during, String startDateFormatted, String endDateFormatted){
        this.id=id;
        this.roomId=roomId;
        this.type=type;
        this.minimumStay=minimumStay;
        this.maximumStay=maximumStay;
        this.startDate=startDate;
        this.endDate=endDate;
        this.during=during;
        this.startDateFormatted=startDateFormatted;
        this.endDateFormatted=endDateFormatted;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMinimumStay() {
        return minimumStay;
    }

    public void setMinimumStay(String minimumStay) {
        this.minimumStay = minimumStay;
    }

    public String getMaximumStay() {
        return maximumStay;
    }

    public void setMaximumStay(String maximumStay) {
        this.maximumStay = maximumStay;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getDuring() {
        return during;
    }

    public void setDuring(String during) {
        this.during = during;
    }

    public String getStartDateFormatted() {
        return startDateFormatted;
    }

    public void setStartDateFormatted(String startDateFormatted) {
        this.startDateFormatted = startDateFormatted;
    }

    public String getEndDateFormatted() {
        return endDateFormatted;
    }

    public void setEndDateFormatted(String endDateFormatted) {
        this.endDateFormatted = endDateFormatted;
    }
}
