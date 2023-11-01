package com.makent.trioangle.spacebooking.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetAvailableTime :Serializable{
    @SerializedName("status_code")
    var statusCode =""

    @SerializedName("success_message")
    var successMessage =""

    @SerializedName("start_date")
    var startDate =""

    @SerializedName("end_date")
    var EndDate =""

    @SerializedName("start_times")
    var startTimes=ArrayList<GetStartEndTimes>()

    @SerializedName("end_times")
    var endTimes=ArrayList<GetStartEndTimes>()

}