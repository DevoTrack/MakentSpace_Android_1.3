package com.makent.trioangle.model.host.addphotos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by trioangle on 10/8/18.
 */

public class RemoveImageResult {

    @SerializedName("success_message")
    @Expose
    private String statusMessage;

    @SerializedName("status_code")
    @Expose
    private String statusCode;

    @SerializedName("room_thumb_images")
    @Expose
    private String[] roomThumbImages;

    @SerializedName("room_image_id")
    @Expose
    private String[] roomImageId;

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String[] getRoomThumbImages() {
        return roomThumbImages;
    }

    public void setRoomThumbImages(String[] roomThumbImages) {
        this.roomThumbImages = roomThumbImages;
    }

    public String[] getRoomImageId() {
        return roomImageId;
    }

    public void setRoomImageId(String[] roomImageId) {
        this.roomImageId = roomImageId;
    }
}
