package com.makent.trioangle.model.tripsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/*****************************************************************
 Trips Detail Previous Trip Result Model Class
 ****************************************************************/


public class TripDetailPreviousResult  implements Serializable {
    //previous_trips

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("previous_bookings")
    @Expose
    private ArrayList<TripDetailData> previousTrips;

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

    public ArrayList<TripDetailData> getPreviousTrips() {
        return previousTrips;
    }

    public void setPreviousTrips(ArrayList<TripDetailData> previousTrips) {
        this.previousTrips = previousTrips;
    }
}
