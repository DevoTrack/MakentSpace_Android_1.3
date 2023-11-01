package com.makent.trioangle.createspace.model.hostlisting.readytohost


import com.google.gson.annotations.SerializedName

data class ChangeCurrency(
    @SerializedName("currency_code")
    var currencyCode: String,
    @SerializedName("currency_symbol")
    var currencySymbol: String,
    @SerializedName("minimum_amount")
    var minimumAmount: Int,
    @SerializedName("status_code")
    var statusCode: String,
    @SerializedName("success_message")
    var successMessage: String
)