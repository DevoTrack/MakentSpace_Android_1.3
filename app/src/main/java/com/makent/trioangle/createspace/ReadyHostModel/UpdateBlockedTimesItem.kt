package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UpdateBlockedTimesItem : Serializable {

    @SerializedName("end_date")
    val endDate: String? = null

    @SerializedName("start_time")
    val startTime: String? = null

    @SerializedName("end_time")
    val endTime: String? = null

    @SerializedName("source")
    val source: String? = null

    @SerializedName("start_date")
    val startDate: String? = null

    @SerializedName("notes")
    val notes: String? = null
}