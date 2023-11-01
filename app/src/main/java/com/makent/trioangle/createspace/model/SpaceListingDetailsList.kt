package com.makent.trioangle.createspace.model

import com.google.gson.annotations.SerializedName

class SpaceListingDetailsList {


    @SerializedName("space_id")
    lateinit var space_id: String

    @SerializedName("name")
    lateinit var name: String

    @SerializedName("status")
    lateinit var status: String

    @SerializedName("admin_status")
    lateinit var admin_status: String

    @SerializedName("photo_name")
    lateinit var photo_name: String

    @SerializedName("completed")
    lateinit var completed: String

    @SerializedName("remaining_steps")
    lateinit var remainingSteps: String


}
