package com.makent.trioangle.model.retrofithost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 8/9/18.
 */

public class OdAmenitiesArray implements Serializable {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("type_id")
    @Expose
    private String type_id;


    private boolean amenitiesselected;


    public boolean getAmenitiesselected() {
        return amenitiesselected;
    }

    public void setAmenitiesselected(boolean amenitiesselected) {
        this.amenitiesselected = amenitiesselected;
    }




    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType_id() {
        return type_id;
    }

    public void setType_id(String type_id) {
        this.type_id = type_id;
    }
}
