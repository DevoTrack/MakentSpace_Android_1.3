package com.makent.trioangle.newhome.makentspacehome.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makent.trioangle.newhome.models.ActivitiesList;

import java.io.Serializable;
import java.util.ArrayList;

public class HostActivityModel implements Serializable {

    @SerializedName("success_message")
    @Expose
    private String successMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("activities")
    @Expose
    private ArrayList<ActivitiesList> activitiesLists;

    @SerializedName("space_types")
    @Expose
    private ArrayList<SpaceTypes> spaceTypes;

    @SerializedName("amenities")
    @Expose
    private ArrayList<Amenities> amenities;

    @SerializedName("services")
    @Expose
    private ArrayList<Services> services;

    @SerializedName("space_styles")
    @Expose
    private ArrayList<Space_styles> space_styles;

    @SerializedName("special_features")
    @Expose
    private ArrayList<Special_features> special_features;

    @SerializedName("space_rules")
    @Expose
    private ArrayList<Space_rules> space_rules;

    public String getSuccessMessage() {
        return successMessage;
    }

    public void setSuccessMessage(String successMessage) {
        this.successMessage = successMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public ArrayList<ActivitiesList> getActivitiesLists() {
        return activitiesLists;
    }

    public void setActivitiesLists(ArrayList<ActivitiesList> activitiesLists) {
        this.activitiesLists = activitiesLists;
    }

    public ArrayList<SpaceTypes> getSpaceTypes() {
        return spaceTypes;
    }

    public void setSpaceTypes(ArrayList<SpaceTypes> spaceTypes) {
        this.spaceTypes = spaceTypes;
    }

    public ArrayList<Amenities> getAmenities() {
        return amenities;
    }

    public void setAmenities(ArrayList<Amenities> amenities) {
        this.amenities = amenities;
    }

    public ArrayList<Services> getServices() {
        return services;
    }

    public void setServices(ArrayList<Services> services) {
        this.services = services;
    }

    public ArrayList<Space_styles> getSpace_styles() {
        return space_styles;
    }

    public void setSpace_styles(ArrayList<Space_styles> space_styles) {
        this.space_styles = space_styles;
    }

    public ArrayList<Special_features> getSpecial_features() {
        return special_features;
    }

    public void setSpecial_features(ArrayList<Special_features> special_features) {
        this.special_features = special_features;
    }

    public ArrayList<Space_rules> getSpace_rules() {
        return space_rules;
    }

    public void setSpace_rules(ArrayList<Space_rules> space_rules) {
        this.space_rules = space_rules;
    }
}
