package com.makent.trioangle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makent.trioangle.host.RoomsBeds.BedTypesAPIList;
import com.makent.trioangle.host.RoomsBeds.BedTypesList;
import com.makent.trioangle.model.homeModels.BedRoomBed;

import java.io.Serializable;
import java.util.ArrayList;

public class BedTypeModel implements Serializable {


    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("data")
    @Expose
    private BedTypeList data;

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

    public BedTypeList getData() {
        return data;
    }

    public void setData(BedTypeList data) {
        this.data = data;
    }
}
