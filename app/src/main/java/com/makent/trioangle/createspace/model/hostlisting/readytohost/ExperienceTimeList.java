package com.makent.trioangle.createspace.model.hostlisting.readytohost;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ExperienceTimeList {
    @SerializedName("id")
    @Expose
    private String id;


    @SerializedName("start_time")
    @Expose
    private String startTime;

    @SerializedName("end_time")
    @Expose
    private String endTime;

    private List<SpaceTimings> subList = new ArrayList<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SpaceTimings> getSubList() {
        return subList;
    }

    public void setSubList(List<SpaceTimings> subList) {
        this.subList = subList;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }
}
