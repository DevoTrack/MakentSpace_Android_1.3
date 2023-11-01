package com.makent.trioangle.model.roomModels;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 1/8/18.
 */

public class RoomSimilarModel implements Serializable {

    @SerializedName("room_id")
    @Expose
    private String roomId;

    @SerializedName("user_id")
    @Expose
    private String userId;

    @SerializedName("room_price")
    @Expose
    private String roomPrice;

    @SerializedName("room_name")
    @Expose
    private String roomName;

    @SerializedName("city_name")
    @Expose
    private String cityName;

    @SerializedName("room_thumb_image")
    @Expose
    private String roomThumbImage;

    @SerializedName("rating_value")
    @Expose
    private String ratingValue;

    @SerializedName("reviews_value")
    @Expose
    private String reviewsValue;

    @SerializedName("is_whishlist")
    @Expose
    private String isWhishlist;

    @SerializedName("instant_book")
    @Expose
    private String instant_book;

    public String getInstant_book() {
        return instant_book;
    }

    public void setInstant_book(String instant_book) {
        this.instant_book = instant_book;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRoomPrice() {
        return roomPrice;
    }

    public void setRoomPrice(String roomPrice) {
        this.roomPrice = roomPrice;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getRoomThumbImage() {
        return roomThumbImage;
    }

    public void setRoomThumbImage(String roomThumbImage) {
        this.roomThumbImage = roomThumbImage;
    }

    public String getReviewsValue() {
        return reviewsValue;
    }

    public void setReviewsValue(String reviewsValue) {
        this.reviewsValue = reviewsValue;
    }

    public String getIsWhishlist() {
        return isWhishlist;
    }

    public void setIsWhishlist(String isWhishlist) {
        this.isWhishlist = isWhishlist;
    }
}
