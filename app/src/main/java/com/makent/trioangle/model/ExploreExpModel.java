package com.makent.trioangle.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by trioangle on 9/19/18.
 */

public class ExploreExpModel implements Serializable {
    @SerializedName("country_name")
    @Expose
    public String country_name;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("currency_symbol")
    @Expose
    public String currency_symbol;
    @SerializedName("experience_id")
    @Expose
    public String experience_id;
    @SerializedName("experience_price")
    @Expose
    public String experience_price;
    @SerializedName("experience_name")
    @Expose
    public String experience_name;
    @SerializedName("rating")
    @Expose
    public String rating_value;
    @SerializedName("reviews_count")
    @Expose
    public String reviews_count;
    @SerializedName("is_wishlist")
    @Expose
    public String is_wishlist;
    @SerializedName("city_name")
    @Expose
    public String city_name;
    @SerializedName("currency_code")
    @Expose
    public String currency_code;
    @SerializedName("latitude")
    @Expose
    public String latitude;
    @SerializedName("longitude")
    @Expose
    public String longitude;
    @SerializedName("experience_category")
    @Expose
    public String experience_category;
    @SerializedName("photo_name")
    @Expose
    public String experience_thumb_images;

    public ExploreExpModel() {
    }

    public ExploreExpModel(String type) {
        this.type = type;
    }

    public String getCountry_name() {
        return country_name;
    }

    public void setCountry_name(String country_name) {
        this.country_name = country_name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCurrency_symbol() {
        return currency_symbol;
    }

    public void setCurrency_symbol(String currency_symbol) {
        this.currency_symbol = currency_symbol;
    }

    public String getExperience_id() {
        return experience_id;
    }

    public void setExperience_id(String experience_id) {
        this.experience_id = experience_id;
    }

    public String getExperience_price() {
        return experience_price;
    }

    public void setExperience_price(String experience_price) {
        this.experience_price = experience_price;
    }

    public String getExperience_name() {
        return experience_name;
    }

    public void setExperience_name(String experience_name) {
        this.experience_name = experience_name;
    }

    public String getRating_value() {
        return rating_value;
    }

    public void setRating_value(String rating_value) {
        this.rating_value = rating_value;
    }

    public String getReviews_count() {
        return reviews_count;
    }

    public void setReviews_count(String reviews_count) {
        this.reviews_count = reviews_count;
    }

    public String getIs_wishlist() {
        return is_wishlist;
    }

    public void setIs_wishlist(String is_wishlist) {
        this.is_wishlist = is_wishlist;
    }

    public String getCity_name() {
        return city_name;
    }

    public void setCity_name(String city_name) {
        this.city_name = city_name;
    }

    public String getCurrency_code() {
        return currency_code;
    }

    public void setCurrency_code(String currency_code) {
        this.currency_code = currency_code;
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

    public String getExperience_category() {
        return experience_category;
    }

    public void setExperience_category(String experience_category) {
        this.experience_category = experience_category;
    }

    public String getExperience_thumb_images() {
        return experience_thumb_images;
    }

    public void setExperience_thumb_images(String experience_thumb_images) {
        this.experience_thumb_images = experience_thumb_images;
    }
}
