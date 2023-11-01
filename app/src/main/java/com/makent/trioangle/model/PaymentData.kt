package com.makent.trioangle.model

import com.google.gson.annotations.SerializedName
import com.makent.trioangle.spacebooking.model.confirmbooking.PaymentCredentials
import com.makent.trioangle.spacebooking.model.confirmbooking.SpaceBookingData
import java.io.Serializable

class PaymentData:Serializable {

    @SerializedName("status_code")
    var statusCode: String = "0"

    @SerializedName("success_message")
    var successMessage: String = ""

    @SerializedName("data")
    var spaceBookingData= SpaceBookingData()

    @SerializedName("payment_credentials")
    var paymentCredentials= PaymentCredentials()


}
