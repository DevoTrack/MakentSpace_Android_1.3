package com.makent.trioangle.spacebooking.model

import com.google.gson.annotations.SerializedName

class BookingActivitiesTypeItems {
    @SerializedName("image_url")
    val imageUrl: String? = null

    @SerializedName("is_selected")
    var isSelected: Boolean = false

    @SerializedName("activities")
    val activities: List<BookingActivitiesItem?>? = null

    @SerializedName("name")
    val name: String? = null

    @SerializedName("id")
    val id="0"

    @SerializedName("currency_symbol")
    val currencySymbol=""

    @SerializedName("hourly")
    val hourlyPrice=""

}