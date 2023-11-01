package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SelectedTimesItem : Serializable {

	@SerializedName("start_time")
	lateinit var startTime: String

	@SerializedName("end_time")
	lateinit var endTime: String

	@SerializedName("id")
	lateinit var id: String
}