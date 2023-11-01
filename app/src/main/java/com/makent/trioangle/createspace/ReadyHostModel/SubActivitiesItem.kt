package com.makent.trioangle.createspace.ReadyHostModel

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SubActivitiesItem : Serializable {

	@SerializedName("is_selected")
    var isSelected: Boolean? = null

	@SerializedName("name")
	val name: String? = null

	@SerializedName("id")
	val id: Int? = null
}