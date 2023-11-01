package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AvailableTimesItem:Serializable {

	@SerializedName("end_date")
	var endDate: String? = null

	@SerializedName("start_time")
	var startTime: String? = null

	@SerializedName("end_time")
	var endTime: String? = null

	@SerializedName("source")
	val source: String? = null

	@SerializedName("start_date")
	var startDate: String? = null

	@SerializedName("notes")
	var notes: String? = null
}