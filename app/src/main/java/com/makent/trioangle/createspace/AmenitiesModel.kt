package com.makent.trioangle.createspace

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.*


class AmenitiesModel : Serializable{
    @SerializedName("id") val id : String=""
    @SerializedName("name") val name : String=""
    @SerializedName("description") val description : String=""
    @SerializedName("icon") val icon : String=""
    @SerializedName("mobile_icon") val mobile_icon : String=""
    @SerializedName("status") val status : String=""
    @SerializedName("image_name") val image_name : String=""

    var isSelected: Boolean = false

}