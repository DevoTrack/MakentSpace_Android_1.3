package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ActivityPriceItem : Serializable {

	@SerializedName("activity_name")
	val activityName: String? = null

	@SerializedName("min_hours")
	val minHours: Int? = null

	@SerializedName("currency_symbol")
	val currencySymbol: String? = null

	@SerializedName("image_url")
	val imageUrl: String? = null

	@SerializedName("full_day")
	val fullDay: Int? = null

	@SerializedName("activity_id")
	val activityId: Int? = null

	@SerializedName("hourly")
	val hourly: Int? = null

	@SerializedName("currency_code")
	val currencyCode: String? = null
}