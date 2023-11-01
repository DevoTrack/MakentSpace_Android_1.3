package com.makent.trioangle.spacebooking.model.confirmbooking

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class SpaceBookingData : Serializable {

    @SerializedName("s_key")
    var sessionId: String = ""

    @SerializedName("space_id")
    var spaceId: String = ""

    @SerializedName("number_of_guests")
    var numberOfGuests: String = ""

    @SerializedName("coupon_applied")
    var couponApplied: Boolean = false

    @SerializedName("booking_type")
    var bookingType: String = ""

    @SerializedName("booking_date_times")
    var bookingDateTimesData = BookingDateTimes()

    @SerializedName("space_name")
    var spaceName: String = ""

    @SerializedName("space_type_name")
    var spaceTypeName: String = ""

    @SerializedName("space_address")
    var spaceAddress: String = ""


    @SerializedName("host_user_name")
    var hostUserName: String = ""

    @SerializedName("host_thumb_image")
    var hostThumbImage: String = ""

    @SerializedName("activity_type")
    var activityType: String = ""

    @SerializedName("total_hours")
    var totalHours: String = ""

    @SerializedName("total_price")
    var totalPrice: String = ""

    @SerializedName("payment_total")
    var paymentTotal: String = ""

    @SerializedName("currency_symbol")
    var currencySymbol: String = ""

    @SerializedName("currency_code")
    var currencyCode: String = ""

    @SerializedName("payment_currency")
    var payableCurrency: String = ""

    @SerializedName("payment_price")
    var payableAmount: String = ""


    class BookingDateTimes : Serializable {
        @SerializedName("start_date")
        var startDate: String = ""

        @SerializedName("end_date")
        var endDate: String = ""

        @SerializedName("end_time")
        var endTime: String = ""

        @SerializedName("start_time")
        var startTime: String = ""
    }
}