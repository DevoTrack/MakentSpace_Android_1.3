package com.makent.trioangle.createspace

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SpacePhotoModelClass : Serializable {


    @SerializedName("id")
    var id: String = ""

    @SerializedName("highlights")
    var highlights: String = ""

    @SerializedName("image_url")
    var imageUrl: String = ""
}
