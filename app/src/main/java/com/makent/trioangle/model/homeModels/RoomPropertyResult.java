package com.makent.trioangle.model.homeModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by trioangle on 31/7/18.
 */

public class RoomPropertyResult  {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("room_type")
    @Expose
    private ArrayList<RoomTypeModel> roomTypeModels;

    @SerializedName("property_type")
    @Expose
    private ArrayList<PropertyTypeModel> propertyTypeModels;

    @SerializedName("bed_type")
    @Expose
    private ArrayList<BedRoomBed> bedTypeModels;

    @SerializedName("length_of_stay_options")
    @Expose
    private ArrayList<LengthOfStayModel> lengthOfStayModels;

    @SerializedName("availability_rules_options")
    @Expose
    private ArrayList<AvailabilityRulesOptionModel> availabilityRulesOptionModels;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String status_message) {
        this.statusMessage = status_message;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<RoomTypeModel> getRoomTypeModels() {
        return roomTypeModels;
    }

    public void setRoomTypeModels(ArrayList<RoomTypeModel> roomTypeModels) {
        this.roomTypeModels = roomTypeModels;
    }

    public ArrayList<PropertyTypeModel> getPropertyTypeModels() {
        return propertyTypeModels;
    }

    public void setPropertyTypeModels(ArrayList<PropertyTypeModel> propertyTypeModels) {
        this.propertyTypeModels = propertyTypeModels;
    }

    public ArrayList<BedRoomBed> getBedTypeModels() {
        return bedTypeModels;
    }

    public void setBedTypeModels(ArrayList<BedRoomBed> bedTypeModels) {
        this.bedTypeModels = bedTypeModels;
    }

    public ArrayList<LengthOfStayModel> getLengthOfStayModels() {
        return lengthOfStayModels;
    }

    public void setLengthOfStayModels(ArrayList<LengthOfStayModel> lengthOfStayModels) {
        this.lengthOfStayModels = lengthOfStayModels;
    }

    public ArrayList<AvailabilityRulesOptionModel> getAvailabilityRulesOptionModels() {
        return availabilityRulesOptionModels;
    }

    public void setAvailabilityRulesOptionModels(ArrayList<AvailabilityRulesOptionModel> availabilityRulesOptionModels) {
        this.availabilityRulesOptionModels = availabilityRulesOptionModels;
    }
}
