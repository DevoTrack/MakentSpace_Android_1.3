package com.makent.trioangle.createspace.model.hostlisting.basics;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makent.trioangle.createspace.AmenitiesModel;
import com.makent.trioangle.createspace.GuestAccessModel;
import com.makent.trioangle.createspace.ServiceModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class BasicStepsModel implements Serializable {

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("success_message")
    @Expose
    private String successMessage;

    @SerializedName("space_types")
    @Expose
    private ArrayList<SpaceTypeList> spaceTypes=new ArrayList<>();

    @SerializedName("guest_accesses")
    @Expose
    private ArrayList<GuestAccessModel> guestAccess=new ArrayList<>();

    @SerializedName("amenities")
    @Expose
    private ArrayList<AmenitiesModel> amenitiesList=new ArrayList<>();

    @SerializedName("services")
    @Expose
    private ArrayList<ServiceModel> serviceList=new ArrayList<>();


    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public ArrayList<SpaceTypeList> getSpaceTypes() {
        return spaceTypes;
    }

    public void setSpaceTypes(ArrayList<SpaceTypeList> spaceTypes) {
        this.spaceTypes = spaceTypes;
    }

    public ArrayList<GuestAccessModel> getGuestAccess() {
        return guestAccess;
    }

    public void setGuestAccess(ArrayList<GuestAccessModel> guestAccess) {
        this.guestAccess = guestAccess;
    }

    public ArrayList<AmenitiesModel> getAmenitiesList() {
        return amenitiesList;
    }

    public void setAmenitiesList(ArrayList<AmenitiesModel> amenitiesList) {
        this.amenitiesList = amenitiesList;
    }

    public ArrayList<ServiceModel> getServiceList() {
        return serviceList;
    }

    public void setServiceList(ArrayList<ServiceModel> serviceList) {
        this.serviceList = serviceList;
    }
}
