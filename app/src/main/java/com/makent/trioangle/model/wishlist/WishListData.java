package com.makent.trioangle.model.wishlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/*****************************************************************
 Wishlist Data Model Class
 ****************************************************************/


public class WishListData  {

    @SerializedName("list_id")
    @Expose
    private String listId;

    @SerializedName("list_name")
    @Expose
    private String listName;


    @SerializedName("space_count")
    @Expose
    private int spaceCount;

    @SerializedName("space_thumb_images")
    @Expose
    private ArrayList spaceThumbImages;

    @SerializedName("privacy")
    @Expose
    private String privacy;


    public WishListData(String listId){
        this.listId =listId;
    }

    public String getListId() {
        return listId;
    }

    public void setListId(String listId) {
        this.listId = listId;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public int getSpaceCount() {
        return spaceCount;
    }

    public void setSpaceCount(int spaceCount) {
        this.spaceCount = spaceCount;
    }

    public ArrayList getSpaceThumbImages() {
        return spaceThumbImages;
    }

    public void setSpaceThumbImages(ArrayList spaceThumbImages) {
        this.spaceThumbImages = spaceThumbImages;
    }

    public String getPrivacy() {
        return privacy;
    }

    public void setPrivacy(String privacy) {
        this.privacy = privacy;
    }
}
