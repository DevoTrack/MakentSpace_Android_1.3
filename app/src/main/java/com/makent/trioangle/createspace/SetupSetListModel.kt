package com.makent.trioangle.createspace

import com.google.gson.annotations.SerializedName

class SetupSetListModel {


    @SerializedName("status_code")
    var statusCode: String = ""

    @SerializedName("success_message")
    var successMessage: String = ""

    @SerializedName("space_styles")
    lateinit var spaceStyles: List<SpacesStyleList>

    @SerializedName("special_features")
    lateinit var specialFeatures: List<SpecailFeaturesList>

    @SerializedName("space_rules")
    lateinit var spaceRules: List<SpaceRulesList>





}
