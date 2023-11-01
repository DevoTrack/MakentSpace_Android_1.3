package com.makent.trioangle.spacebooking.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class GetStartEndTimes : Serializable{

    @SerializedName("time")
    var timeValue =""


    @SerializedName("is_blocked")
    var isBlocked =false

    var isSelected =false
}