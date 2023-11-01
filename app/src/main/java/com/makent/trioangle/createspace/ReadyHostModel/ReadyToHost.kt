package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.makent.trioangle.createspace.CancelltionPolicy
import java.io.Serializable

class ReadyToHost: Serializable {

	@SerializedName("availability_times")
	lateinit var availabilityTimes: ArrayList<AvailabilityTimesItem>

	@SerializedName("security")
	lateinit var security: String

	@SerializedName("activity_types")
	var activityTypes: List<ActivityTypesItem?>? = null

	@SerializedName("calendar_data")
	var calendarData: CalendarData? = null

	@SerializedName("cancellation_policy")
	lateinit var cancellationPolicy: ArrayList<CancelltionPolicy>

	@SerializedName("booking_type")
	lateinit var bookingType: String



	@SerializedName("activity_price")
	var activityPrice: List<ActivityPriceItem?>? = null

	@SerializedName("minimum_amount")
	var minimumAmount: Int? = null

	@SerializedName("currency_code")
	var currencyCode: String? = null

	@SerializedName("status")
	@Expose
	var status: String=""

	@SerializedName("remaining_steps")
	@Expose
	var remainingSteps: String=""
}