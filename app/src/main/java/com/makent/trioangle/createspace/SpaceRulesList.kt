package com.makent.trioangle.createspace

import com.google.gson.annotations.SerializedName

class SpaceRulesList {

    @SerializedName("id")
    var id: String = ""

    @SerializedName("name")
    var name: String = ""

    @SerializedName("status")
    var status: String = ""

    var isSelected: Boolean = false

}
