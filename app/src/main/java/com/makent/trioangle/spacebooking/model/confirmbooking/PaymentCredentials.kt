package com.makent.trioangle.spacebooking.model.confirmbooking

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PaymentCredentials :Serializable{

    @SerializedName("PAYPAL_LIVE_MODE")
    var isPayPalLiveMode= false

    @SerializedName("PAYPAL_CLIENT_ID")
    var paypalClientId: String = ""

    @SerializedName("STRIPE_PUBLISH_KEY")
    var stripePublishKey: String = ""

}