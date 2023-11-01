package com.makent.trioangle.spacebooking.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SpaceAvailabilityTimings :Serializable{

    @SerializedName("day_type")
    var datType:Int=0

    @SerializedName("key")
    var key=""

    @SerializedName("status")
    var status=""

    @SerializedName("value")
    var value=""
}