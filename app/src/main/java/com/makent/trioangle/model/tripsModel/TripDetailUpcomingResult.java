package com.makent.trioangle.model.tripsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;


/*****************************************************************
 Trips Detail Upcoming Trip Result Model Class
 ****************************************************************/


public class TripDetailUpcomingResult {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("upcoming_bookings")
    @Expose
    private ArrayList<TripDetailData> upcomingtrips;

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

    public ArrayList<TripDetailData> getUpcomingtrips() {
        return upcomingtrips;
    }

    public void setUpcomingtrips(ArrayList<TripDetailData> upcomingtrips) {
        this.upcomingtrips = upcomingtrips;
    }
}
