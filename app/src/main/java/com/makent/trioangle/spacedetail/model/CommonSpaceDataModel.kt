package com.makent.trioangle.spacedetail.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class CommonSpaceDataModel : Serializable{

    @SerializedName("key")
    var key = ""

    @SerializedName("value")
    var value = ""
}