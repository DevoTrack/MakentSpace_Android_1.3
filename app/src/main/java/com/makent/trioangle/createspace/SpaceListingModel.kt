package com.makent.trioangle.createspace

import com.google.gson.annotations.SerializedName
import com.makent.trioangle.createspace.ReadyHostModel.ReadyToHost
import java.io.Serializable


class SpaceListingModel : Serializable {
    @SerializedName("status_code")
    var id: String = "0"

    @SerializedName("success_message")
    var successMessage: String = ""

    @SerializedName("space_id")
    var spaceId: String = ""

    @SerializedName("first_name")
    var firstName: String = ""

    @SerializedName("basics")
    lateinit var basics: BasicStepModel


    @SerializedName("setup")
    lateinit var setup: SetupStepModel


    @SerializedName("ready_to_host")
    lateinit var ready_to_host: ReadyToHost

    @SerializedName("status")
    var status: String? = null

    @SerializedName("admin_status")
    var admin_status: String = ""

    @SerializedName("completed")
    var completed: Int = 0



}


