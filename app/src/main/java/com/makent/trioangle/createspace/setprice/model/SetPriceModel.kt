package com.makent.trioangle.createspace.setprice.model


import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SetPriceModel : Serializable{
    @SerializedName("activity_price")
    lateinit var activityPrice: List<ActivityPrice>

    @SerializedName("currency_code")
    lateinit var currencyCode: String
    @SerializedName("currency_symbol")
    lateinit var currencySymbol: String
    @SerializedName("minimum_amount")
    var minimumAmount: Int=0

    @SerializedName("status_code")
    lateinit var statusCode: String
    @SerializedName("success_message")
    lateinit var successMessage: String
}