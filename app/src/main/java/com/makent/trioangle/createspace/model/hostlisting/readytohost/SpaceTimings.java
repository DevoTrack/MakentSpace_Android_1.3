package com.makent.trioangle.createspace.model.hostlisting.readytohost;

import com.makent.trioangle.helper.Constants;
import com.makent.trioangle.host.RoomsBeds.BaseModel;

import java.io.Serializable;

public class SpaceTimings implements Serializable, BaseModel {

    private int id;

    private String time;

    private String timeZone;

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public int getViewType() {
        return Constants.ViewType.MultipleTime;
    }
}
