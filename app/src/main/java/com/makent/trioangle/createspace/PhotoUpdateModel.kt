package com.makent.trioangle.createspace

import com.google.gson.annotations.SerializedName

class PhotoUpdateModel {


    @SerializedName("status_code")
    var statusCode: String = ""

    @SerializedName("success_message")
    var successMessage: String = ""

    @SerializedName("photos_list")
    lateinit var photos_list: List<SpacePhotoModelClass>



}
