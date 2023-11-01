package com.makent.trioangle.model.retrofithost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by trioangle on 8/9/18.
 */

public class OdAmenitiesModel implements Serializable {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;


    @SerializedName("data")
    @Expose
    private ArrayList<OdAmenitiesArray> data;









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

    public ArrayList<OdAmenitiesArray> getData() {
        return data;
    }

    public void setData(ArrayList<OdAmenitiesArray> data) {
        this.data = data;
    }
}
