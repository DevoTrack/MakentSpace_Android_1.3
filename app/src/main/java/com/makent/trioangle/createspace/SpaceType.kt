package com.makent.trioangle.createspace

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SpaceType : Serializable {
    @SerializedName("id")
    @Expose
    val id: String = ""
    @SerializedName("name")
    @Expose
    val name: String = ""
}