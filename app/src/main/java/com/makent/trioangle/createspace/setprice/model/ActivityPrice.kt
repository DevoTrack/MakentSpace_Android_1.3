package com.makent.trioangle.createspace.setprice.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ActivityPrice : Serializable{
    @SerializedName("activity_id")
    var activityId: Int = 0
    @SerializedName("activity_name")
    var activityName: String = ""
    @SerializedName("currency_code")
    var currencyCode: String = ""
    @SerializedName("currency_symbol")
    var currencySymbol: String = ""
    @SerializedName("full_day")
    var fullDay: Long = 0
    @SerializedName("hourly")
    var hourly: Long = 0
    @SerializedName("weekly")
    var weekly: Long = 0
    @SerializedName("monthly")
    var monthly: Long = 0
    @SerializedName("image_url")
    var imageUrl: String = ""
    @SerializedName("min_hours")
    var minHours: Int = 0
}