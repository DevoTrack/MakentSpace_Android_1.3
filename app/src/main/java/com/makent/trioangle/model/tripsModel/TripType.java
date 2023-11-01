package com.makent.trioangle.model.tripsModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/*****************************************************************
 Trips Type Model Class
 ****************************************************************/


public class TripType {

    @SerializedName("Trips_type")
    @Expose
    private String tripsType;

    @SerializedName("Trips_count")
    @Expose
    private String tripsCount;

    public TripType(){
    }

    public String getTripsType() {
        return tripsType;
    }

    public void setTripsType(String tripsType) {
        this.tripsType = tripsType;
    }

    public String getTripsCount() {
        return tripsCount;
    }

    public void setTripsCount(String tripsCount) {
        this.tripsCount = tripsCount;
    }
}
