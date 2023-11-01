package com.makent.trioangle.newhome.makentspacehome.Model;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class HomeListModel implements Serializable {
    @SerializedName("space_id")
    @Expose
    public String spaceid;

    @SerializedName("name")
    @Expose
    public String name;

    @SerializedName("space_type_name")
    @Expose
    public String spaceTypename;


    @SerializedName("size")
    @Expose
    public String size;

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getSize_type() {
        return size_type;
    }

    public void setSize_type(String size_type) {
        this.size_type = size_type;
    }

    @SerializedName("size_type")
    @Expose
    public String size_type;

    @SerializedName("photo_name")
    @Expose
    public String image;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("is_wishlist")
    @Expose
    public String isWishlist;

    @SerializedName("reviews_count")
    @Expose
    public String reviewscount;

    @SerializedName("currency_code")
    @Expose
    public String currencyCode;

    @SerializedName("currency_symbol")
    @Expose
    public String currencySymbol;

    @SerializedName("city_name")
    @Expose
    public String cityName;

    @SerializedName("hourly")
    @Expose
    public String hourly;

    @SerializedName("country_name")
    @Expose
    public String countryName;

    @SerializedName("latitude")
    @Expose
    public String latitude;

    @SerializedName("longitude")
    @Expose
    public String longitude;

    @SerializedName("instant_book")
    @Expose
    public String instantBook;

    @SerializedName("type")
    @Expose
    public String type;

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HomeListModel(String type) {
        this.type = type;
    }

    public String getSpaceid() {
        return spaceid;
    }

    public void setSpaceid(String spaceid) {
        this.spaceid = spaceid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpaceTypename() {
        return spaceTypename;
    }

    public void setSpaceTypename(String spaceTypename) {
        this.spaceTypename = spaceTypename;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getIsWishlist() {
        return isWishlist;
    }

    public void setIsWishlist(String isWishlist) {
        this.isWishlist = isWishlist;
    }

    public String getReviewscount() {
        return reviewscount;
    }

    public void setReviewscount(String reviewscount) {
        this.reviewscount = reviewscount;
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

    public String getHourly() {
        return hourly;
    }

    public void setHourly(String hourly) {
        this.hourly = hourly;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
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

    public String getInstantBook() {
        return instantBook;
    }

    public void setInstantBook(String instantBook) {
        this.instantBook = instantBook;
    }
}
