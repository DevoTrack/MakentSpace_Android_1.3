package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UpdateModel : Serializable{

    @SerializedName("status_code")
    var id :String = "1"

    @SerializedName("success_message")
    var successmsg : String  = ""

    @SerializedName("calendar_data")
    var updateCalendarData : CalendarData? = null

}