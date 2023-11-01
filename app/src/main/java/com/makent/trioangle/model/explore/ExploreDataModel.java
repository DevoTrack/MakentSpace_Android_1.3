package com.makent.trioangle.model.explore;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


/*****************************************************************
 Explore Model Class
 ****************************************************************/


public class ExploreDataModel implements Serializable {

    @SerializedName("room_id")
    @Expose
    private String roomId;

    @SerializedName("room_type")
    @Expose
    private String roomType;

    @SerializedName("room_price")
    @Expose
    private String roomPrice;

    @SerializedName("room_name")
    @Expose
    private String roomName;

    @SerializedName("photo_name")
    @Expose
    private String roomThumbImage;

    @SerializedName("rating")
    @Expose
    private String ratingValue;

    @SerializedName("reviews_count")
    @Expose
    private String reviewsCount;

    @SerializedName("instant_book")
    @Expose
    private String instantBook;

    @SerializedName("latitude")
    @Expose
    private String latitude;

    @SerializedName("longitude")
    @Expose
    private String longitude;

    @SerializedName("is_wishlist")
    @Expose
    private String isWishlist;

    @SerializedName("country_name")
    @Expose
    private String countryName;

    @SerializedName("city_name")
    @Expose
    private String cityName;

    @SerializedName("currency_code")
    @Expose
    private String currencyCode;

    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;

    @SerializedName("type")
    @Expose
    private String type;

    public ExploreDataModel(String type){
        this.type=type;
    }

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
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

    public String getRoomThumbImage() {
        return roomThumbImage;
    }

    public void setRoomThumbImage(String roomThumbImage) {
        this.roomThumbImage = roomThumbImage;
    }

    public String getRatingValue() {
        return ratingValue;
    }

    public void setRatingValue(String ratingValue) {
        this.ratingValue = ratingValue;
    }

    public String getReviewsCount() {
        return reviewsCount;
    }

    public void setReviewsCount(String reviewsCount) {
        this.reviewsCount = reviewsCount;
    }

    public String getInstantBook() {
        return instantBook;
    }

    public void setInstantBook(String instantBook) {
        this.instantBook = instantBook;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(String isWishlist) {
        this.isWishlist = isWishlist;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
