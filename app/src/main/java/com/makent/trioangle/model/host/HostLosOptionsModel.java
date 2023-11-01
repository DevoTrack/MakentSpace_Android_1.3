package com.makent.trioangle.model.host;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 8/6/18.
 */

public class HostLosOptionsModel implements Serializable {

    @SerializedName("nights")
    @Expose
    private String nights;

    @SerializedName("text")
    @Expose
    private String text;


    public String getNights() {
        return nights;
    }

    public void setNights(String nights) {
        this.nights = nights;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
