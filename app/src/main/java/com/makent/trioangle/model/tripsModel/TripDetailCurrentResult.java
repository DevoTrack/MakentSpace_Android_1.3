package com.makent.trioangle.model.tripsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by trioangle on 10/8/18.
 */

public class TripDetailCurrentResult {


    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("current_bookings")
    @Expose
    private ArrayList<TripDetailData> currentTrips;

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

    public ArrayList<TripDetailData> getCurrentTrips() {
        return currentTrips;
    }

    public void setCurrentTrips(ArrayList<TripDetailData> currentTrips) {
        this.currentTrips = currentTrips;
    }
}
