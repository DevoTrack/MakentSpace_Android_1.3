package com.makent.trioangle.createspace

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class AddressModel : Serializable {

    @SerializedName("address_line_1")
    var mAddress1: String = ""
    @SerializedName("address_line_2")
    var mAddress2: String = ""
    @SerializedName("city")
    var mCity: String = ""
    @SerializedName("state")
    var mState: String = ""
    @SerializedName("postal_code")
    var mPostal: String = ""
    @SerializedName("country_name")
    var mCountryName: String = ""
    @SerializedName("country")
    var mCountryCode: String = ""
    @SerializedName("longitude")
    var mlongitude: String = ""
    @SerializedName("latitude")
    var mlatitude: String = ""
    @SerializedName("guidance")
    var mGuidance: String = ""

}