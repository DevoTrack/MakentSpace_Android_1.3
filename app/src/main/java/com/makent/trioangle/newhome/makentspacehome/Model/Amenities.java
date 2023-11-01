package com.makent.trioangle.newhome.makentspacehome.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Amenities {

    @SerializedName("id")
    @Expose
    private String  id;

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("description")
    @Expose
    private String description;

    @SerializedName("icon")
    @Expose
    private String icon;

    @SerializedName("mobile_icon")
    @Expose
    private String mobile_icon;

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("image_name")
    @Expose
    private String image_name;

    public String  getId() {
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getMobile_icon() {
        return mobile_icon;
    }

    public void setMobile_icon(String mobile_icon) {
        this.mobile_icon = mobile_icon;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }
}
