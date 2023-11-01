package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class UpdateCalendar : Serializable{

    @SerializedName("not_available_times")
    val notAvailableTimes: UpdateNotAvailableTimes? = null

    @SerializedName("available_times")
    val availableTimes: List<UpdateAvailableTimesItem?>? = null

    @SerializedName("blocked_times")
    val blockedTimes: List<UpdateBlockedTimesItem?>? = null

    @SerializedName("not_available_days")
    val notAvailableDays: List<Int?>? = null


}