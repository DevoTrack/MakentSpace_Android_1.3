package com.makent.trioangle.spacebooking.model

import com.google.gson.annotations.SerializedName

class BookingActivitiesItem {
    @SerializedName("sub_activities")
    val subActivities: List<BookingSubActivitiesItem?>? = null

    var isSelected: Boolean = false

    @SerializedName("name")
    val name: String? = null

    @SerializedName("id")
    val id = "0"
}