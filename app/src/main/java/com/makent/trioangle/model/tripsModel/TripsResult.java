package com.makent.trioangle.model.tripsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*****************************************************************
 Trips Result Model Class
 ****************************************************************/


public class TripsResult {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("booking_types")
    @Expose
    private ArrayList<BookingTypeModel> bookingTypeModelArrayList;



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

    public ArrayList<BookingTypeModel> getBookingTypeModelArrayList() {
        return bookingTypeModelArrayList;
    }

    public void setBookingTypeModelArrayList(ArrayList<BookingTypeModel> bookingTypeModelArrayList) {
        this.bookingTypeModelArrayList = bookingTypeModelArrayList;
    }
}
