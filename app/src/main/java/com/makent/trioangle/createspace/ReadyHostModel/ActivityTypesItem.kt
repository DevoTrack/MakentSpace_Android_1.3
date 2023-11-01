package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import com.makent.trioangle.spacebooking.model.BookingActivitiesItem
import java.io.Serializable

class ActivityTypesItem : Serializable {

	@SerializedName("image_url")
	val imageUrl: String? = null

	@SerializedName("is_selected")
    var isSelected: Boolean? = null

	@SerializedName("activities")
	val activities: List<ActivitiesItem?>? = null

	@SerializedName("name")
	val name: String? = null

	@SerializedName("id")
	val id: Int? = null
}