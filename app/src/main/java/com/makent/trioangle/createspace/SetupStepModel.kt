package com.makent.trioangle.createspace

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SetupStepModel : Serializable {


    @SerializedName("space_style")
    var spaceStyle: String = ""

    @SerializedName("special_feature")
    var specialFeature: String = ""

    @SerializedName("space_rules")
    var spaceRules: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("summary")
    var summary: String = ""

    @SerializedName("space")
    var space: String = ""

    @SerializedName("access")
    var access: String = ""

    @SerializedName("interaction")
    var interaction: String = ""

    @SerializedName("notes")
    var notes: String = ""

    @SerializedName("house_rules")
    var houseRules: String = ""

    @SerializedName("neighborhood_overview")
    var neighborhoodOverview: String = ""

    @SerializedName("transit")
    var transit: String = ""


    @SerializedName("space_photos")
    var spacePhotos: List<SpacePhotoModelClass>? = null

    @SerializedName("status")
    @Expose
    var status: String=""

    @SerializedName("remaining_steps")
    @Expose
    var remainingSteps: String=""



}
