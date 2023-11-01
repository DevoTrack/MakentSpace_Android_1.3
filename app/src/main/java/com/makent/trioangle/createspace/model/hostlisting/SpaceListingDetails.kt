package com.makent.trioangle.createspace.model.hostlisting

import com.google.gson.annotations.SerializedName
import com.makent.trioangle.createspace.model.SpaceListingDetailsList

class SpaceListingDetails {


    @SerializedName("status_code")
    lateinit var status_code: String

    @SerializedName("success_message")
    lateinit var success_message: String

    @SerializedName("listed")
    lateinit var listed: List<SpaceListingDetailsList>

    @SerializedName("unlisted")
    lateinit var unlisted: List<SpaceListingDetailsList>


}
