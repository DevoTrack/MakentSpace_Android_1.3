package com.makent.trioangle.spacedetail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SpaceDetailArray: Serializable {

    @SerializedName("key")
    @Expose
    var SpacekeyName=""


    @SerializedName("value")
    @Expose
    var SpacekeyValue=""

}