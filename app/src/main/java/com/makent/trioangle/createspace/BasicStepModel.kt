package com.makent.trioangle.createspace

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class BasicStepModel : Serializable {

    @SerializedName("number_of_rooms")
    @Expose
    var numberOfRooms: String = ""
    @SerializedName("number_of_restrooms")
    @Expose
    var numberOfRestrooms: String = ""
    @SerializedName("number_of_guests")
    @Expose
    var numberOfGuests: String = ""

    @SerializedName("fully_furnished")
    @Expose
    var fullyFurnished: String = ""

    @SerializedName("no_of_workstations")
    @Expose
    var noOfWorkstations: String = ""

    @SerializedName("shared_or_private")
    @Expose
    var sharedOrPrivate: String = ""

    @SerializedName("renting_space_firsttime")
    @Expose
    var rentingSpaceFirsttime: String = ""

    @SerializedName("sq_ft")
    @Expose
    var sqFt: String = ""
    @SerializedName("size_type")
    @Expose
    var sizeType: String = ""
    @SerializedName("guest_access")
    @Expose
    var guestAccess: String = ""
    @SerializedName("guest_access_other")
    @Expose
    var guestAccessOther: String = ""
    @SerializedName("amenities")
    @Expose
    var amenities: String = ""
    @SerializedName("services")
    @Expose
    var services: String = ""
    @SerializedName("services_extra")
    @Expose
    var servicesExtra: String = ""
    @SerializedName("space_style")
    @Expose
    var spaceStyle: String = ""
    @SerializedName("address_line_1")
    @Expose
    var addressLine1: String = ""
    @SerializedName("address_line_2")
    @Expose
    var addressLine2: String = ""
    @SerializedName("city")
    @Expose
    var city: String = ""
    @SerializedName("state")
    @Expose
    var state: String = ""
    @SerializedName("country")
    @Expose
    var country: String = ""
    @SerializedName("postal_code")
    @Expose
    var postalCode: String = ""
    @SerializedName("latitude")
    @Expose
    var latitude: String = ""
    @SerializedName("longitude")
    @Expose
    var longitude: String = ""
    @SerializedName("guidance")
    @Expose
    var guidance: String = ""

    @SerializedName("space_type")
    @Expose
    var spaceType: SpaceType =SpaceType()

    @SerializedName("status")
    @Expose
    var status: String=""

    @SerializedName("remaining_steps")
    @Expose
    var remainingSteps: String=""

}
