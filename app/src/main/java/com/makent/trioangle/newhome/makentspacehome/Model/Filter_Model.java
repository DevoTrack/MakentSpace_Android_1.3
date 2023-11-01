package com.makent.trioangle.newhome.makentspacehome.Model;

public class Filter_Model {
    private String id;
    private String name;
    private String description;
    private String icon;
    private String mobile_icon;
    private boolean status;
    private boolean isSelected;
    private String image_name;
    private String amenitystate;


    public Filter_Model(){

    }

    public Filter_Model(String id, String name, String description, String icon, String mobile_icon, boolean status, String image_name, String amenitystate,boolean isSelected) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.mobile_icon = mobile_icon;
        this.status = status;
        this.image_name = image_name;
        this.amenitystate = amenitystate;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
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

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getAmenitystate() {
        return amenitystate;
    }

    public void setAmenitystate(String amenitystate) {
        this.amenitystate = amenitystate;
    }
}
