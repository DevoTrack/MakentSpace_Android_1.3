package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UpdatedCalenderData :Serializable{


    @SerializedName("status_code")
    val status_code = ""

    @SerializedName("success_message")
    val success_message = ""

    @SerializedName("calendar_data")
    var calendarData=CalendarData()
}