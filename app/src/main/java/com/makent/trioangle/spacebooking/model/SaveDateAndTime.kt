package com.makent.trioangle.spacebooking.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SaveDateAndTime : Serializable{

    @SerializedName("start_date")
    var StartDate=""

    @SerializedName("end_date")
    var endDate=""

    @SerializedName("start_time")
    var startTime=""

    @SerializedName("end_time")
    var endTime=""
}