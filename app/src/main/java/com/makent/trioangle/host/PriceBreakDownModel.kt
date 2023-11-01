package com.makent.trioangle.host

import com.google.gson.annotations.SerializedName
import com.makent.trioangle.createspace.CancelltionPolicy
import com.makent.trioangle.model.PriceBreakDownList

class PriceBreakDownModel {

    @SerializedName("status_code")
    var id: String = "0"

    @SerializedName("success_message")
    var successMessage: String = ""

    @SerializedName("data")
    lateinit var priceBreakDownList: ArrayList<PriceBreakDownList>

}
