package com.makent.trioangle.spacedetail.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SpacesAccessServicesAndOthers : Serializable{

    @SerializedName("id")
    @Expose
    var id = ""

    @SerializedName("name")
    @Expose
    var name = ""

    @SerializedName("image_name")
    @Expose
    var imageName = ""

}