
package com.makent.trioangle.newhome.models;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Lists implements Serializable
{

    @SerializedName("Title")
    @Expose
    private String title;

    @SerializedName("Key")
    @Expose
    private String Key;

    @SerializedName("Details")
    @Expose
    private ArrayList<Detail> details = null;
    private final static long serialVersionUID = 8470315348321432895L;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<Detail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<Detail> details) {
        this.details = details;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }
}
