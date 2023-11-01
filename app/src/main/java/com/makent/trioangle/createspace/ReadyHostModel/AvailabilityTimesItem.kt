package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AvailabilityTimesItem : Serializable {

	@SerializedName("id")
	var id: Int = 0

	@SerializedName("availability_times")
	lateinit var selectedTimes: ArrayList<SelectedTimesItem>

	@SerializedName("day")
	var day: Int = 0

	@SerializedName("status")
	lateinit var status: String
}