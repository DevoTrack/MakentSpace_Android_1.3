package com.makent.trioangle.createspace

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class ServiceModel: Serializable{
    @SerializedName("id")
    @Expose
    var id: String = ""
    @SerializedName("name")
    @Expose
    var name: String = ""
    @SerializedName("status")
    @Expose
    var status: String = ""

    var isSelected: Boolean = false

}
