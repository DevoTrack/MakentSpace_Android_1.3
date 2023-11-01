package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CalendarData : Serializable {

	@SerializedName("not_available_times")
	val notAvailableTimes: NotAvailableTimes? = null

	@SerializedName("available_times")
	val availableTimes: List<AvailableTimesItem?>? = null

	@SerializedName("blocked_times")
	val blockedTimes: List<BlockedTimesItem?>? = null

}