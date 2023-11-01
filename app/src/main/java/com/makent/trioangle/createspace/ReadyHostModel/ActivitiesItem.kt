package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ActivitiesItem: Serializable {

	@SerializedName("sub_activities")
	val subActivities: List<SubActivitiesItem?>? = null

	@SerializedName("is_selected")
	var isSelected: Boolean? = null

	@SerializedName("name")
	val name: String? = null

	@SerializedName("id")
	val id: Int? = null
}