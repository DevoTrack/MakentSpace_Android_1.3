package com.makent.trioangle.spacedetail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SpaceActivityArray : Serializable {
    @SerializedName("id")
    @Expose
    val Id =""

    @SerializedName("name")
    @Expose
    val Name =""

    @SerializedName("hourly")
    @Expose
    val priceHourly =""

    @SerializedName("min_hours")
    @Expose
    val priceMinHours =""

    @SerializedName("full_day")
    @Expose
    val priceFullDay =""

    @SerializedName("currency_code")
    @Expose
    val CurrencyCode =""

    @SerializedName("currency_symbol")
    @Expose
    val CurrencySymbol =""

    @SerializedName("image_url")
    @Expose
    val imageurl ="imageUrl"
}