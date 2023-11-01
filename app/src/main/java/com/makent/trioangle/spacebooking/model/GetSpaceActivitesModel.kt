package com.makent.trioangle.spacebooking.model

import com.google.gson.annotations.SerializedName

class GetSpaceActivitesModel {

    @SerializedName("status_code")
    var StatusCode = ""

    @SerializedName("status_message")
    var StatusMessage = ""

    @SerializedName("activity_types")
    var activityTypes: List<BookingActivitiesTypeItems?>? = null
}