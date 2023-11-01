package com.makent.trioangle.host.RoomsBeds;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.makent.trioangle.helper.Constants;

import java.io.Serializable;

public class BedTypesList implements Serializable, BaseModel {


    @SerializedName("name")
    @Expose
    String name;

    @SerializedName("count")
    @Expose
    int count;

    @SerializedName("id")
    @Expose
    int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    @Override
    public int getViewType() {
        return Constants.ViewType.BedType;
    }
}
