package com.makent.trioangle.model.host;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 8/6/18.
 */

public class HostLosRulesModel implements Serializable {


    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("room_id")
    @Expose
    private String roomId;

    @SerializedName("type")
    @Expose
    private String type;

    @SerializedName("period")
    @Expose
    private String period;

    @SerializedName("discount")
    @Expose
    private String discount;

    @SerializedName("period_text")
    @Expose
    private String period_text;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getPeriod_text() {
        return period_text;
    }

    public void setPeriod_text(String period_text) {
        this.period_text = period_text;
    }
}
